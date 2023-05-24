package dsdms.exam.handlers.theoreticalHandlers

import dsdms.exam.handlers.domainConversionTable
import dsdms.exam.handlers.getHttpCode
import dsdms.exam.model.Model
import dsdms.exam.model.domainServices.DomainResponseStatus
import dsdms.exam.model.entities.theoreticalExam.TheoreticalExamPass
import io.vertx.ext.web.RoutingContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection

class TheoreticalExamHandlersImpl(val model: Model) : TheoreticalExamHandlers {
    override fun createTheoreticalExamPass(routingContext: RoutingContext) {
        try {
            val insertResult = model.examService.saveNewTheoreticalExamPass(Json.decodeFromString(routingContext.body().asString()))
            routingContext.response().setStatusCode(domainConversionTable.getHttpCode(insertResult.domainResponseStatus)).end(
                insertResult.theoreticalExamPass ?: insertResult.domainResponseStatus.toString())
        } catch (ex: SerializationException) {
            routingContext.response().setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST).end(ex.message)
        }
    }

    override fun getTheoreticalExamPass(routingContext: RoutingContext) {
        val retrievedTheoreticalExamPass: TheoreticalExamPass? = model.examService.readTheoreticalExamPass(routingContext.request().getParam("id").toString())
        if (retrievedTheoreticalExamPass == null)
            routingContext.response()
                .setStatusCode(domainConversionTable.getHttpCode(DomainResponseStatus.ID_NOT_FOUND))
                .end(DomainResponseStatus.ID_NOT_FOUND.name)
        else routingContext.response().setStatusCode(HttpURLConnection.HTTP_OK).end(Json.encodeToString(retrievedTheoreticalExamPass))
    }

    override fun deleteTheoreticalExamPass(routingContext: RoutingContext) {
        val deleteResult: DomainResponseStatus = model.examService.deleteTheoreticalExamPass(routingContext.request().getParam("id").toString())
        routingContext.response().setStatusCode(domainConversionTable.getHttpCode(deleteResult)).end(deleteResult.toString())
    }

    override fun createNewTheoreticalExamDay(routingContext: RoutingContext) {
        try {
            val insertResult: DomainResponseStatus = model.examService.insertNewExamDay(Json.decodeFromString(routingContext.body().asString()))
            routingContext.response().setStatusCode(domainConversionTable.getHttpCode(insertResult)).end(insertResult.toString())
        } catch (ex: SerializationException) {
            routingContext.response().setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST).end(ex.message)
        }
    }
}