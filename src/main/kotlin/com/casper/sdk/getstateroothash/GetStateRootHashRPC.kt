package com.casper.sdk.getstateroothash

import com.casper.sdk.ConstValues
import net.jemzart.jsonkraken.get
import net.jemzart.jsonkraken.toJson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**Class built for chain_get_state_root_hash RPC call
 */
class GetStateRootHashRPC {

    fun testMethod() {
        println("This is a test method called")
    }
    var methodUrl: String = ConstValues.TESTNET_URL
    /**
     * This function initiate the process of sending POST request with given parameter in JSON string format
     * The input parameterStr is used to send to server as parameter of the POST request to get the result back.
     * The input parameterStr is somehow like this:
     * {"params" :  [], "id" :  1, "method": "chain_get_state_root_hash", "jsonrpc" :  "2.0"}
     * if you wish to send without any param along with the RPC call
     * or:
     * {"method" :  "chain_get_state_root_hash", "id" :  1, "params" :  {"block_identifier" :  {"Hash" : "d16cb633eea197fec519aee2cfe050fe9a3b7e390642ccae8366455cc91c822e"}}, "jsonrpc" :  "2.0"}
     * if you wish to send the block hash along with the POST method in the RPC call
     * or:
     * {"method" :  "chain_get_state_root_hash", "id" :  1, "params" :  {"block_identifier" :  {"Height" : 100}}, "jsonrpc" :  "2.0"}
     * if you wish to send the block height along with the POST method in the RPC call
     * Then the state root hash is retrieved by parsing JsonObject result
     * If the result is error,  then an exception is thrown
     * Else the state root hash is taken by parsing the  retrieving JsonObject
     */
    @Throws(IllegalArgumentException:: class)
    fun getStateRootHash(parameterStr: String) : String {
        println("Parameter is: " + parameterStr)
        val url = URL( ConstValues.TESTNET_URL)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.setRequestMethod("POST")
        con.setRequestProperty("Content-Type", "application/json")
        con.setRequestProperty("Accept", "application/json");
        con.doOutput = true
        con.outputStream.use { os ->
            val input: ByteArray = parameterStr.toByteArray()
            os.write(input, 0, input.size)
        }
        BufferedReader(
            InputStreamReader(con.inputStream, "utf-8")
        ).use {
            val response = StringBuilder()
            var responseLine: String? = null
            while (it.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }
            println(response.toString())
            val jsonBack = response.toString().toJson()
            if(jsonBack.get("error") != null) {
                throw IllegalArgumentException("Error get state root hash")
            } else {
                val stateRootHash = jsonBack.get("result").get("state_root_hash").toString()
                println("State root hash get back is:" + stateRootHash)
                return stateRootHash
            }
        }
        /*val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(this.methodUrl))
            .POST((HttpRequest.BodyPublishers.ofString(parameterStr)))
            .header("Content-Type",  "application/json")
            .build()
        val response = client.send(request,  HttpResponse.BodyHandlers.ofString())
        val json =response.body().toJson()
        //Check for error
        if(json.get("error") != null) {
            throw IllegalArgumentException("Error get state root hash")
        } else { //If not error then get the state root hash
            val stateRootHash = json.get("result").get("state_root_hash").toString()
            println("State root hash get back is:" + stateRootHash)
            return stateRootHash
        }*/
    }
}