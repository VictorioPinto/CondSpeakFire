package com.example.condspeak.services

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreService {
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getCollection(collectionName: String) = firestore.collection(collectionName)
}
