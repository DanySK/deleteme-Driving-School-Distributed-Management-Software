package dsdms.dossier.model

import dsdms.dossier.model.domainServices.DossierDomainService

/**
 * Main model of the system.
 */
interface Model {

    /**
     * Representing actually used model implementation.
     */
    val dossierDomainService: DossierDomainService
}
