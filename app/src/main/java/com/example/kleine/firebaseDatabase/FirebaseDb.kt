package com.example.kleine.firebaseDatabase

import android.util.Log
import com.example.kleine.model.Address
import com.example.kleine.model.CartProduct
import com.example.kleine.model.Product
import com.example.kleine.model.User
import com.example.kleine.util.Constants.Companion.ADDRESS_COLLECTION
import com.example.kleine.util.Constants.Companion.BEST_DEALS
import com.example.kleine.util.Constants.Companion.CART_COLLECTION
import com.example.kleine.util.Constants.Companion.CATEGORIES_COLLECTION
import com.example.kleine.util.Constants.Companion.CATEGORY
import com.example.kleine.util.Constants.Companion.CHAIR_CATEGORY
import com.example.kleine.util.Constants.Companion.CLOTHES
import com.example.kleine.util.Constants.Companion.COLOR
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.ID
import com.example.kleine.util.Constants.Companion.ORDERS
import com.example.kleine.util.Constants.Companion.PRICE
import com.example.kleine.util.Constants.Companion.PRODUCTS_COLLECTION
import com.example.kleine.util.Constants.Companion.QUANTITY
import com.example.kleine.util.Constants.Companion.SIZE
import com.example.kleine.util.Constants.Companion.TITLE
import com.example.kleine.util.Constants.Companion.USERS_COLLECTION
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDb {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)
    private val categoriesCollection = Firebase.firestore.collection(CATEGORIES_COLLECTION)

    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    private val userCartCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(CART_COLLECTION)
    }
    private val userAddressesCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(ADDRESS_COLLECTION)

    }


    private val firebaseAuth = Firebase.auth

    fun createNewUser(
        email: String, password: String
    ) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun saveUserInformation(
        userUid: String,
        user: User
    ) = usersCollectionRef.document(userUid).set(user)

    fun loginUser(
        email: String,
        password: String
    ) = firebaseAuth.signInWithEmailAndPassword(email, password)

    fun getClothesProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CLOTHES).limit(pagingPage).get()

    fun getBestDealsProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, BEST_DEALS).limit(pagingPage).get()

    fun getChairs(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CHAIR_CATEGORY).limit(pagingPage).get()

    //add order by orders
    fun getMostOrderedCupboard(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .orderBy(ORDERS, Query.Direction.DESCENDING).limit(pagingPage).get()

    fun getCupboards(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .limit(pagingPage).get()

    fun addProductToCart(product: CartProduct) = userCartCollection?.document()!!.set(product)

    fun getProductInCart(product: CartProduct) = userCartCollection!!
        .whereEqualTo(ID, product.id)
        .whereEqualTo(COLOR, product.color)
        .whereEqualTo(SIZE, product.size).get()

    fun increaseProductQuantity(documentId: String): Task<Transaction> {
        val document = userCartCollection!!.document(documentId)
        return Firebase.firestore.runTransaction { transaction ->
            val productBefore = transaction.get(document)
            var quantity = productBefore.getLong(QUANTITY)
            quantity = quantity!! + 1
            transaction.update(document, QUANTITY, quantity)
        }

    }

    fun getItemsInCart() = userCartCollection!!

    fun decreaseProductQuantity(documentId: String): Task<Transaction> {
        val document = userCartCollection!!.document(documentId)
        return Firebase.firestore.runTransaction { transaction ->
            val productBefore = transaction.get(document)
            var quantity = productBefore.getLong(QUANTITY)
            quantity = if (quantity!!.toInt() == 1)
                1
            else
                quantity!! - 1
            transaction.update(document, QUANTITY, quantity)

        }

    }

    fun deleteProductFromCart(documentId: String) =
        userCartCollection!!.document(documentId).delete()


    fun searchProducts(searchQuery: String) = productsCollection
        .orderBy("title")
        .startAt(searchQuery)
        .endAt("\u03A9+$searchQuery")
        .limit(5)
        .get()

    fun getCategories() = categoriesCollection.get()

    fun getProductFromCartProduct(cartProduct: CartProduct) =
        productsCollection.whereEqualTo(ID, cartProduct.id)
            .whereEqualTo(TITLE, cartProduct.name)
            .whereEqualTo(PRICE,cartProduct.price).get()

    fun saveNewAddress(address: Address) = userAddressesCollection?.add(address)

    fun getAddresses() = userAddressesCollection
}