package dsdms.driving.database

import com.mongodb.client.result.DeleteResult
import dsdms.driving.database.utils.RepositoryResponseStatus
import dsdms.driving.model.entities.DrivingSlot
import dsdms.driving.model.entities.Instructor
import dsdms.driving.model.entities.Vehicle
import dsdms.driving.model.valueObjects.DrivingSlotsRequest
import dsdms.driving.model.valueObjects.LicensePlate
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.time.LocalDate

class RepositoryImpl(drivingService: CoroutineDatabase) : Repository {
    private val drivingSlots = drivingService.getCollection<DrivingSlot>("DrivingSlot")
    private val vehicles = drivingService.getCollection<Vehicle>("Vehicle")
    private val instructors = drivingService.getCollection<Instructor>("Instructor")

    override suspend fun createDrivingSlot(newDrivingSlot: DrivingSlot): String? {
        return newDrivingSlot.apply { drivingSlots.insertOne(newDrivingSlot) }._id
    }

    override suspend fun getOccupiedDrivingSlots(docs: DrivingSlotsRequest): List<DrivingSlot> {
        return if (docs.instructorId == null) {
            drivingSlots.find(DrivingSlot::date eq docs.date.toString()).toList()
        } else {
            drivingSlots.find(and(DrivingSlot::date eq docs.date.toString(), DrivingSlot::instructorId eq docs.instructorId)).toList()
        }
    }

    override suspend fun getFutureDrivingSlots(): List<DrivingSlot> {
        return drivingSlots.find().toList().filter { el -> LocalDate.parse(el.date) > LocalDate.now() }
    }

    override suspend fun doesVehicleExist(licensePlate: LicensePlate): Boolean {
        return vehicles.find(Vehicle::licensePlate eq licensePlate).toList().isNotEmpty()
    }

    override suspend fun doesInstructorExist(instructorId: String): Boolean {
        return instructors.find(Instructor::_id eq instructorId).toList().isNotEmpty()
    }

    override suspend fun deleteDrivingSlot(drivingSlotId: String): RepositoryResponseStatus {
        return handleDeleteResult(
            runBlocking {
                drivingSlots.deleteOne(DrivingSlot::_id eq drivingSlotId)
            }
        )
    }

    private fun handleDeleteResult(deleteResult: DeleteResult): RepositoryResponseStatus {
        return if (!deleteResult.wasAcknowledged() || deleteResult.deletedCount.toInt() == 0) {
            RepositoryResponseStatus.DELETE_ERROR
        } else {
            RepositoryResponseStatus.OK
        }
    }
}
