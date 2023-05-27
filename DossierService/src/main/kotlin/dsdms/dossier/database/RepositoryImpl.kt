package dsdms.dossier.database

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import dsdms.dossier.database.utils.RepositoryResponseStatus
import dsdms.dossier.model.entities.Dossier
import dsdms.dossier.model.valueObjects.ExamsProgress
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class RepositoryImpl(dossierServiceDb: CoroutineDatabase) : Repository {
    private val dossiers = dossierServiceDb.getCollection<Dossier>("Dossier")

    override suspend fun createDossier(newDossier: Dossier): String? {
        return newDossier.apply { dossiers.insertOne(newDossier) }._id
    }

    override suspend fun readDossierFromId(id: String): Dossier? {
        return dossiers.findOneById(id)
    }

    override suspend fun readDossierFromCf(cf: String): List<Dossier> {
        return dossiers.find(Dossier::fiscal_code eq cf).toList()
    }

    override suspend fun updateExamStatus(newStatus: ExamsProgress?, id: String): RepositoryResponseStatus {
        return handleUpdateResults(dossiers.updateOne((Dossier::_id eq id), setValue(Dossier::examsProgress, newStatus)))
    }

    private fun handleUpdateResults(updateResult: UpdateResult): RepositoryResponseStatus {
        return if (updateResult.modifiedCount.toInt() != 1 || !updateResult.wasAcknowledged()) {
            RepositoryResponseStatus.UPDATE_ERROR
        } else {
            RepositoryResponseStatus.OK
        }
    }

    override suspend fun deleteDossier(id: String): RepositoryResponseStatus {
        return handleDeleteResult(
            runBlocking {
                dossiers.deleteOne(Dossier::_id eq id)
            }
        )
    }

//    override suspend fun updateExamAttempts(dossierId: String, examAttempts: PracticalExamAttempts): RepositoryResponseStatus {
//        return handleUpdateResults(dossiers.updateOne((Dossier::_id eq dossierId), setValue(Dossier::examAttempts, examAttempts)))
//    }

    private fun handleDeleteResult(deleteResult: DeleteResult): RepositoryResponseStatus {
        return if (!deleteResult.wasAcknowledged() || deleteResult.deletedCount.toInt() == 0) {
            RepositoryResponseStatus.DELETE_ERROR
        } else {
            RepositoryResponseStatus.OK
        }
    }
}
