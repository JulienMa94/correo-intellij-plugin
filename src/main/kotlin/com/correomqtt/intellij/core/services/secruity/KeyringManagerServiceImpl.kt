package com.correomqtt.intellij.core.services.secruity

import com.correomqtt.intellij.GuiCore
import org.apache.maven.artifact.versioning.ComparableVersion
import org.correomqtt.core.fileprovider.SecretStoreProvider
import org.correomqtt.core.keyring.Keyring
import org.correomqtt.core.keyring.KeyringFactory
import org.correomqtt.core.model.SettingsDTO
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.di.SoyDi
import java.util.*

class KeyringManagerServiceImpl : KeyringManagerService {

    private val guiCore: GuiCore = SoyDi.inject(GuiCore::class.java)
    private val settingsManager: SettingsManager = guiCore.getSettingsManager()
    private val secretStoreProvider: SecretStoreProvider = guiCore.getSecretStoreProvider()
    private val keyringFactory: KeyringFactory = guiCore.getKeyringFactory()
    private var masterPassword: String? = null
    private var keyring: Keyring? = null
    val KEYRING_LABEL: String = "CorreoMQTT_MasterPassword"

    override fun migrate(newKeyringIdentifier: String) {
        TODO("Not yet implemented")
    }

    override fun getMasterPassword(): String {

        if (masterPassword != null) {
           return masterPassword!!
        }

        if (keyring != null) {
            if (keyring!!.requiresUserinput()) {
                TODO("Alert keyring requires user input must show alert dialog")
            } else {
                masterPassword = keyring!!.getPassword(KEYRING_LABEL)
                if (masterPassword == null || masterPassword!!.isEmpty()) {
                    keyring!!.setPassword(
                        KEYRING_LABEL,
                        UUID.randomUUID().toString()
                    )
                    masterPassword = keyring!!.getPassword(KEYRING_LABEL)
                }
            }
        }

        return masterPassword!!
    }

    override fun init() {
        val settings: SettingsDTO = settingsManager.settings
        val oldKeyringIdentifier = settings.keyringIdentifier

        keyring = null

        if (oldKeyringIdentifier != null) {
            keyring = keyringFactory.createKeyringByIdentifier(oldKeyringIdentifier)
        }

        if (keyring == null) {
            val keyrings: MutableList<Keyring> = keyringFactory.create()

            if (keyrings.count() <= 2) {
                keyring = keyrings.first()
            } else {
                TODO("Alert multiple keyrings found must show alert dialog")
            }
        }

        if (keyring == null) {
            TODO("Alert not supported keyring found must show alert dialog")
        }

        val newKeyringIdentifier = keyring!!.identifier

        val createdVersion = ComparableVersion(settings.configCreatedWithCorreoVersion.replace("[^0-9.]", ""));
        val keyringSupportVersion = ComparableVersion("0.13.0");

        if (oldKeyringIdentifier == null && keyringSupportVersion.compareTo(createdVersion) < 0) {
           TODO("Alert keyring not supported must show alert dialog")
        } else if (newKeyringIdentifier != oldKeyringIdentifier) {
           TODO("Alert keyring migration must show alert dialog")
        }

        if (!newKeyringIdentifier.equals(oldKeyringIdentifier)) {
            settings.keyringIdentifier = newKeyringIdentifier
            settingsManager.saveSettings()
        }
    }

    override fun wipe() {
        masterPassword = null
        secretStoreProvider.wipe()
    }
}