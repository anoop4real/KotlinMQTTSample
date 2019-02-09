package com.example.anoopm.mqtt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.anoopm.mqtt.manager.MQTTConnectionParams
import com.example.anoopm.mqtt.manager.MQTTmanager
import com.example.anoopm.mqtt.protocols.UIUpdaterInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UIUpdaterInterface {

    var mqttManager:MQTTmanager? = null

    // Interface methods
    override fun resetUIWithConnection(status: Boolean) {

        ipAddressField.isEnabled  = !status
        topicField.isEnabled      = !status
        messageField.isEnabled    = status
        connectBtn.isEnabled      = !status
        sendBtn.isEnabled         = status

        // Update the status label.
        if (status){
            updateStatusViewWith("Connected")
        }else{
            updateStatusViewWith("Disconnected")
        }
    }

    override fun updateStatusViewWith(status: String) {
        statusLabl.text = status
    }

    override fun update(message: String) {

        var text = messageHistoryView.text.toString()
        var newText = """
            $text
            $message
            """
        //var newText = text.toString() + "\n" + message +  "\n"
        messageHistoryView.setText(newText)
        messageHistoryView.setSelection(messageHistoryView.text.length)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable send button and message textfield only after connection
        resetUIWithConnection(false)
    }

    fun connect(view: View){

        if (!(ipAddressField.text.isNullOrEmpty() && topicField.text.isNullOrEmpty())) {
            var host = "tcp://" + ipAddressField.text.toString() + ":1883"
            var topic = topicField.text.toString()
            var connectionParams = MQTTConnectionParams("MQTTSample",host,topic,"","")
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager?.connect()
        }else{
            updateStatusViewWith("Please enter all valid fields")
        }

    }

    fun sendMessage(view: View){

        mqttManager?.publish(messageField.text.toString())

        messageField.setText("")
    }
}
