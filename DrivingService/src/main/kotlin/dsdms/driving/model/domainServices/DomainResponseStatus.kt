package dsdms.driving.model.domainServices

enum class DomainResponseStatus {
    OK,
    NO_SLOT_OCCUPIED,
    INSTRUCTOR_NOT_FREE,
    VEHICLE_NOT_FREE,
    INVALID_PROVISIONAL_LICENSE,
    OCCUPIED_DRIVING_SLOTS,
    BAD_VEHICLE_INSTRUCTOR_INFO,
    NO_PROVISIONAL_LICENSE,
}
