package dsdms.dossier.service

import java.net.HttpURLConnection.*

enum class Errors {
    OK,
    FISCAL_CODE_DUPLICATION,
    ID_NOT_FOUND,
    DELETE_ERROR,
    UPDATE_ERROR,
    MULTIPLE_EQUAL_IDS
}

val conversionTable: Map<Errors, Int> = mapOf(
    Errors.OK to HTTP_OK,
    Errors.FISCAL_CODE_DUPLICATION to HTTP_CONFLICT,
    Errors.ID_NOT_FOUND to HTTP_NOT_FOUND,
    Errors.DELETE_ERROR to HTTP_NOT_MODIFIED,
    Errors.UPDATE_ERROR to HTTP_NOT_MODIFIED,
    Errors.MULTIPLE_EQUAL_IDS to HTTP_MULT_CHOICE
)

fun Map<Errors, Int>.getHttpCode(errors: Errors): Int {
    return conversionTable.getOrDefault(errors, HTTP_INTERNAL_ERROR)
}