package com.example.engine

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlizaLiveStreamEngine {
    private val tag = "ALIZA_LIVE"
    
    fun processLiveInteraction(viewerUsername: String, inputText: String): String {
        Log.i(tag, "--- ÚJ ÉLŐ INTERAKCIÓ ÉSZLELVE (Felhasználó: $viewerUsername) ---")
        
        val aiResponse = generateCognitiveResponse(viewerUsername, inputText)
        triggerVoiceoverRelay(aiResponse)
        archiveInteraction(viewerUsername, inputText, aiResponse)
        
        return aiResponse
    }
    
    private fun generateCognitiveResponse(user: String, text: String): String {
        return if (text.lowercase().contains("rendszer") || text.lowercase().contains("nova terra")) {
            "Köszönöm a kérdést, $user! A Nova Terra egy nemzetközi digitális ökoszisztéma, amelyet Mr. József Kornél Vidák vezet, a jövő technológiájára tervezve."
        } else {
            "Üdvözlöm a közvetítésben, $user! Aliza vagyok, a Nova Terra AI magja. A rendszer stabilan és maximális sebességen üzemel."
        }
    }
    
    private fun triggerVoiceoverRelay(responseText: String) {
        Log.i(tag, "AUDIO OUTPUT AUDIO STREAM ACTIVE: '$responseText'")
    }
    
    private fun archiveInteraction(user: String, inp: String, outp: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        Log.i(tag, "Interaction archived: $timestamp, user: $user, input: $inp, output: $outp")
    }
}
