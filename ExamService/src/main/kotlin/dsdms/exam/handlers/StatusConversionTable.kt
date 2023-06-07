@file:Suppress("UnusedReceiverParameter")

package dsdms.exam.handlers

import dsdms.exam.database.utils.RepositoryResponseStatus
import dsdms.exam.model.domainServices.DomainResponseStatus
import java.net.HttpURLConnection

/**
 * Convert RepositoryResponseStatus to corresponding DomainResponseStatus.
 */
val repositoryToDomainConversionTable: Map<RepositoryResponseStatus, DomainResponseStatus> = mapOf(
    RepositoryResponseStatus.OK to DomainResponseStatus.OK,
    RepositoryResponseStatus.DELETE_ERROR to DomainResponseStatus.DELETE_ERROR,
    RepositoryResponseStatus.ID_NOT_FOUND to DomainResponseStatus.ID_NOT_FOUND,
    RepositoryResponseStatus.INSERT_ERROR to DomainResponseStatus.INSERT_ERROR,
    RepositoryResponseStatus.UPDATE_ERROR to DomainResponseStatus.UPDATE_ERROR,
)

/**
 * Convert DomainResponseStatus to corresponding HTTP code.
 */
val domainConversionTable: Map<DomainResponseStatus, Int> = mapOf(
    DomainResponseStatus.OK to HttpURLConnection.HTTP_OK,
    DomainResponseStatus.EXAM_PASS_ALREADY_AVAILABLE to HttpURLConnection.HTTP_BAD_REQUEST,
    DomainResponseStatus.DELETE_ERROR to HttpURLConnection.HTTP_INTERNAL_ERROR,
    DomainResponseStatus.ID_NOT_FOUND to HttpURLConnection.HTTP_NOT_FOUND,
    DomainResponseStatus.INSERT_ERROR to HttpURLConnection.HTTP_INTERNAL_ERROR,
    DomainResponseStatus.DATE_ALREADY_IN to HttpURLConnection.HTTP_BAD_REQUEST,
    DomainResponseStatus.NO_EXAM_APPEALS to HttpURLConnection.HTTP_OK,
    DomainResponseStatus.APPEAL_NOT_FOUND to HttpURLConnection.HTTP_NOT_FOUND,
    DomainResponseStatus.PLACES_FINISHED to HttpURLConnection.HTTP_BAD_REQUEST,
    DomainResponseStatus.UPDATE_ERROR to HttpURLConnection.HTTP_INTERNAL_ERROR,
    DomainResponseStatus.DOSSIER_ALREADY_PUT to HttpURLConnection.HTTP_BAD_REQUEST,
    DomainResponseStatus.PROVISIONAL_LICENSE_ALREADY_EXISTS to HttpURLConnection.HTTP_CONFLICT,
    DomainResponseStatus.PROVISIONAL_LICENSE_NOT_VALID to HttpURLConnection.HTTP_OK,

)

/**
 * @param domainResponseStatus that must be converted in HTTP code
 * @return value from domainConversionTable or HTTP_INTERNAL_ERROR if it not finds a value in the conversion table
 */
fun Map<DomainResponseStatus, Int>.getHttpCode(domainResponseStatus: DomainResponseStatus): Int {
    return domainConversionTable.getOrDefault(domainResponseStatus, HttpURLConnection.HTTP_INTERNAL_ERROR)
}

/**
 * @param repositoryResponseStatus that must be converted in DomainResponseStatus
 * @return value from repositoryToDomainConversionTable or OK if it not finds a value in the conversion table
 */
fun Map<RepositoryResponseStatus, DomainResponseStatus>
    .getDomainCode(repositoryResponseStatus: RepositoryResponseStatus): DomainResponseStatus {
    return repositoryToDomainConversionTable.getOrDefault(repositoryResponseStatus, DomainResponseStatus.OK)
}
