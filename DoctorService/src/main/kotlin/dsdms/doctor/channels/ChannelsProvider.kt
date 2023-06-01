package dsdms.doctor.channels

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions


interface ChannelsProvider {
    val dossierServiceChannel: DossierServiceChannel
    val examServiceChannel: ExamServiceChannel
}

class ChannelsProviderImpl(private val vertx: Vertx): ChannelsProvider {
    override val dossierServiceChannel: DossierServiceChannel
    override val examServiceChannel: ExamServiceChannel

    companion object {
        const val LOCALHOST: String = "localhost"
        const val DEFAULT_DOSSIER_SERVICE_PORT = 8000
        const val DEFAULT_EXAM_SERVICE_PORT = 8020
    }

    init {
        dossierServiceChannel = getDossierServiceClient()
        examServiceChannel = getExamServiceClient()
    }

    private fun getDossierServiceClient(): DossierServiceChannel {
        val client = createClient(getHost("dossier_host"), getPort("dossier_port", DEFAULT_DOSSIER_SERVICE_PORT))
        return DossierServiceChannelImpl(client)
    }

    private fun getExamServiceClient(): ExamServiceChannel {
        val client = createClient(getHost("exam_host"), getPort("exam_port", DEFAULT_EXAM_SERVICE_PORT))
        return ExamServiceChannelImpl(client)
    }

    private fun getHost(hostName: String ): String{
        return if (System.getProperty(hostName) != null) System.getProperty(hostName) else LOCALHOST
    }

    private fun getPort(portName: String, defaultPort: Int): Int {
        return  if (System.getProperty(portName) != null) System.getProperty(portName).toInt() else defaultPort
    }

    private fun createClient(host: String, port: Int): WebClient {
        val options: WebClientOptions = WebClientOptions()
            .setDefaultPort(port)
            .setDefaultHost(host)
        return WebClient.create(vertx, options)
    }
}