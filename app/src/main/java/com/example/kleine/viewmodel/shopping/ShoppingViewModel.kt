package com.example.kleine.viewmodel.shopping

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.*
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.ACCESSORY_CATEGORY
import com.example.kleine.util.Constants.Companion.CHAIR_CATEGORY
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.FURNITURE_CATEGORY
import com.example.kleine.util.Constants.Companion.TABLES_CATEGORY
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class ShoppingViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {
    private val TAG = "ShoppingViewModel"

    val clothes = MutableLiveData<List<Product>>()
    val emptyClothes = MutableLiveData<Boolean>()
    val bestDeals = MutableLiveData<List<Product>>()
    val emptyBestDeals = MutableLiveData<Boolean>()

    val home = MutableLiveData<Resource<List<Product>>>()

    val chairs = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedChairs = MutableLiveData<Resource<List<Product>>>()

    val tables = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedTables = MutableLiveData<Resource<List<Product>>>()

    val accessory = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedAccessories = MutableLiveData<Resource<List<Product>>>()

    val furniture = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedFurniture = MutableLiveData<Resource<List<Product>>>()

    val mostCupboardOrdered = MutableLiveData<Resource<List<Product>>>()
    val cupboard = MutableLiveData<Resource<List<Product>>>()
    val addToCart = MutableLiveData<Resource<Boolean>>()

    val addAddress = MutableLiveData<Resource<Address>>()
    val updateAddress = MutableLiveData<Resource<Address>>()
    val deleteAddress = MutableLiveData<Resource<Address>>()

    val profile = MutableLiveData<Resource<User>>()

    val uploadProfileImage = MutableLiveData<Resource<String>>()
    val updateUserInformation = MutableLiveData<Resource<User>>()

    val userOrders = MutableLiveData<Resource<List<Order>>>()

    val passwordReset = MutableLiveData<Resource<String>>()

    val orderAddress = MutableLiveData<Resource<Address>>()
    val orderProducts = MutableLiveData<Resource<List<CartProduct>>>()

    val categories = MutableLiveData<Resource<List<Category>>>()

    val bestProducts = ArrayList<MutableLiveData<Resource<List<Product>>>>()
    val mostRequestedProducts = ArrayList<MutableLiveData<Resource<List<Product>>>>()


    private var homePage: Long = 10
    private var clothesPaging: Long = 5
    private var bestDealsPaging: Long = 5

    private var cupboardPaging: Long = 4
    private var mostOrderCupboardPaging: Long = 5

    private var mostRequestedChairsPage: Long = 3
    private var chairsPage: Long = 4

    private var mostRequestedTablePage: Long = 3
    private var tablePage: Long = 4

    private var mostRequestedAccessoryPage: Long = 3
    private var accessoryPage: Long = 4

    private var mostRequestedFurniturePage: Long = 3
    private var furniturePage: Long = 4


    init {
//        getCategories() { categories ->
//            var i =0
//            categories.forEach {
//                bestProducts.add(MutableLiveData())
//                mostRequestedProducts.add(MutableLiveData())
//                getProductsByCategory(it.name,i)
//                getMostRequestedProducts(it.name,i)
//                i++
//            }

        getClothesProducts()
        getBestDealsProduct()
        getHomeProduct(10)
        getCupboardsByOrders(3)
        getCupboardProduct(4)
        getUser()

        getChairs(4)
        getMostRequestedChairs(3)

        getTables(4)
        getMostRequestedTables(3)

        getAccessories(4)
        getMostRequestedAccessories(3)

        getFurniture(4)
        getMostRequestedFurniture(3)

    }

    fun getFurniture(size: Int) {
        furniture.postValue(Resource.Loading())
        shouldPaging(FURNITURE_CATEGORY, size) {
            if (it) {
                firebaseDatabase.getProductsByCategory(FURNITURE_CATEGORY, furniturePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                furniture.postValue(Resource.Success(productsList))
                                furniturePage += 4

                            }
                        } else
                            furniture.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }


    fun getMostRequestedFurniture(size: Int) {
        mostRequestedFurniture.postValue(Resource.Loading())
        shouldPaging(FURNITURE_CATEGORY, size) {
            if (it) {
                firebaseDatabase.getProductsByCategory(FURNITURE_CATEGORY, furniturePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedFurniture.postValue(Resource.Success(productsList))
                                furniturePage += 4

                            }
                        } else
                            mostRequestedFurniture.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }

    fun getAccessories(size: Int) {
        accessory.postValue(Resource.Loading())
        shouldPaging(ACCESSORY_CATEGORY, size) {
            if (it) {
                firebaseDatabase.getProductsByCategory(ACCESSORY_CATEGORY, accessoryPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                accessory.postValue(Resource.Success(productsList))
                                accessoryPage += 4

                            }
                        } else
                            accessory.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }


    fun getMostRequestedAccessories(size: Int) {
        mostRequestedAccessories.postValue(Resource.Loading())
        shouldPaging(ACCESSORY_CATEGORY, size) {
            if (it) {
                chairs.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(ACCESSORY_CATEGORY, mostRequestedAccessoryPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedAccessories.postValue(Resource.Success(productsList))
                                mostRequestedAccessoryPage += 4

                            }
                        } else
                            mostRequestedAccessories.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }

    fun getChairs(size: Int) {
        chairs.postValue(Resource.Loading())
        shouldPaging(CHAIR_CATEGORY, size) {
            if (it) {
                chairs.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(CHAIR_CATEGORY, chairsPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                chairs.postValue(Resource.Success(productsList))
                                chairsPage += 4

                            }
                        } else
                            chairs.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }


    fun getMostRequestedChairs(size: Int) {
        mostRequestedChairs.postValue(Resource.Loading())
        shouldPaging(CHAIR_CATEGORY, size) {
            if (it) {
                chairs.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(CHAIR_CATEGORY, mostRequestedChairsPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedChairs.postValue(Resource.Success(productsList))
                                mostRequestedChairsPage += 4

                            }
                        } else
                            mostRequestedChairs.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }

    fun getTables(size: Int) {
        tables.postValue(Resource.Loading())
        shouldPaging(TABLES_CATEGORY, size) {
            if (it) {
                tables.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(TABLES_CATEGORY, tablePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                tables.postValue(Resource.Success(productsList))
                                tablePage += 4

                            }
                        } else
                            chairs.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }

    fun getMostRequestedTables(size: Int) {
        mostRequestedTables.postValue(Resource.Loading())
        shouldPaging(TABLES_CATEGORY, size) {
            if (it) {
                mostRequestedTables.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(TABLES_CATEGORY, mostRequestedTablePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedTables.postValue(Resource.Success(productsList))
                                mostRequestedTablePage += 3

                            }
                        } else
                            mostRequestedTables.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }


//    fun getProductsByCategory(category:String,position:Int){
//        bestProducts[position].postValue(Resource.Loading())
//        firebaseDatabase.getProductsByCategory(category).addOnCompleteListener {
//            if (it.isSuccessful){
//                val products = it.result.toObjects(Product::class.java)
//                bestProducts[position].postValue(Resource.Success(products))
//
//            }else{
//                bestProducts[position].postValue(Resource.Error(it.exception.toString()))
//            }
//        }
//    }
//
//    fun getMostRequestedProducts(category:String,position:Int){
//        mostRequestedProducts[position].postValue(Resource.Loading())
//        firebaseDatabase.getMostRequestedProducts(category).addOnCompleteListener {
//            if (it.isSuccessful){
//                val products = it.result.toObjects(Product::class.java)
//                mostRequestedProducts[position].postValue(Resource.Success(products))
//
//            }else{
//                mostRequestedProducts[position].postValue(Resource.Error(it.exception.toString()))
//            }
//        }
//    }


//    fun getCategories(onSuccess: (List<Category>) -> Unit) {
//        categories.postValue(Resource.Loading())
//        firebaseDatabase.getCategories().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val categoriesList = it.result.toObjects(Category::class.java)
//                categories.postValue(Resource.Success(categoriesList))
//                onSuccess(categoriesList)
//            } else
//                categories.postValue(Resource.Error(it.exception.toString()))
//        }
//    }


    fun getClothesProducts() =
        firebaseDatabase.getClothesProducts(clothesPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    clothes.postValue(productsList)
                    clothesPaging += 5
                } else
                    emptyClothes.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())

        }

    fun getBestDealsProduct() =
        firebaseDatabase.getBestDealsProducts(bestDealsPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    bestDeals.postValue(productsList)
                    bestDealsPaging += 5
                } else
                    emptyBestDeals.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())
        }

    fun getHomeProduct(size: Int) {
        home.postValue(Resource.Loading())
        shouldPagingHome(size)
        {
            if (it) {
                home.postValue(Resource.Loading())
                firebaseDatabase.getHomeProducts(homePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                home.postValue(Resource.Success(productsList))
                                homePage += 4

                            }
                        } else
                            home.postValue(Resource.Error(it.exception.toString()))
                    }
            }
        }
    }


    fun getCupboardsByOrders(size: Int) =
        shouldPaging(CUPBOARD_CATEGORY, size) {
            if (it) {
                mostCupboardOrdered.postValue(Resource.Loading())
                firebaseDatabase.getMostOrderedCupboard(mostOrderCupboardPaging)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostCupboardOrdered.postValue(Resource.Success(productsList))
                                mostOrderCupboardPaging += 5

                            }
                        } else
                            mostCupboardOrdered.postValue(Resource.Error(it.exception.toString()))

                    }
            }
        }

    fun getCupboardProduct(size: Int) =
        shouldPaging(CUPBOARD_CATEGORY, size) {
            if (it) {
                cupboard.postValue(Resource.Loading())
                firebaseDatabase.getCupboards(cupboardPaging).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val documents = it.result
                        if (!documents!!.isEmpty) {
                            val productsList = documents.toObjects(Product::class.java)
                            cupboard.postValue(Resource.Success(productsList))
                            cupboardPaging += 10
                        }

                    } else
                        cupboard.postValue(Resource.Error(it.exception.toString()))
                }
            }
        }

    /*
    * TODO : Move these functions to firebaseDatabase class
     */

    private fun shouldPaging(category: String, listSize: Int, onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("categories")
            .whereEqualTo("name", category).get().addOnSuccessListener {
                val tempCategory = it.toObjects(Category::class.java)
                val products = tempCategory[0].products
                Log.d("test", " prodcuts ${tempCategory[0].products}, size $listSize")
                if (listSize == products)
                    onSuccess(false)
                else
                    onSuccess(true)
            }
    }

    private fun shouldPagingHome(listSize: Int, onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("categories").get().addOnSuccessListener {
                var productsCount = 0
                it.toObjects(Category::class.java).forEach { category ->
                    productsCount+=category.products!!.toInt()
                }

                if(listSize == productsCount)
                    onSuccess(false)
                else
                    onSuccess(true)

            }
    }


    private fun checkIfProductAlreadyAdded(
        product: CartProduct,
        onSuccess: (Boolean, String) -> Unit
    ) {
        addToCart.postValue(Resource.Loading())
        firebaseDatabase.getProductInCart(product).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result!!.documents
                if (documents.isNotEmpty())
                    onSuccess(true, documents[0].id) // true ---> product is already in cart
                else
                    onSuccess(false, "") // false ---> product is not in cart
            } else
                addToCart.postValue(Resource.Error(it.exception.toString()))

        }
    }


    fun addProductToCart(product: CartProduct) =
        checkIfProductAlreadyAdded(product) { isAdded, id ->
            if (isAdded) {
                firebaseDatabase.increaseProductQuantity(id).addOnCompleteListener {
                    if (it.isSuccessful)
                        addToCart.postValue(Resource.Success(true))
                    else
                        addToCart.postValue(Resource.Error(it.exception!!.message))

                }
            } else {
                firebaseDatabase.addProductToCart(product).addOnCompleteListener {
                    if (it.isSuccessful)
                        addToCart.postValue(Resource.Success(true))
                    else
                        addToCart.postValue(Resource.Error(it.exception!!.message))
                }
            }
        }


    fun saveAddress(address: Address) {
        addAddress.postValue(Resource.Loading())
        firebaseDatabase.saveNewAddress(address)?.addOnCompleteListener {
            if (it.isSuccessful)
                addAddress.postValue(Resource.Success(address))
            else
                addAddress.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun updateAddress(oldAddress: Address, newAddress: Address) {
        updateAddress.postValue(Resource.Loading())
        firebaseDatabase.findAddress(oldAddress).addOnCompleteListener { addressResponse ->
            if (addressResponse.isSuccessful) {
                val documentUid = addressResponse.result!!.documents[0].id
                firebaseDatabase.updateAddress(documentUid, newAddress)?.addOnCompleteListener {
                    if (it.isSuccessful)
                        updateAddress.postValue(Resource.Success(newAddress))
                    else
                        updateAddress.postValue(Resource.Error(it.exception.toString()))

                }

            } else
                updateAddress.postValue(Resource.Error(addressResponse.exception.toString()))

        }
    }

    fun deleteAddress(address: Address) {
        deleteAddress.postValue(Resource.Loading())
        firebaseDatabase.findAddress(address).addOnCompleteListener { addressResponse ->
            if (addressResponse.isSuccessful) {
                val documentUid = addressResponse.result!!.documents[0].id
                firebaseDatabase.deleteAddress(documentUid, address)?.addOnCompleteListener {
                    if (it.isSuccessful)
                        deleteAddress.postValue(Resource.Success(address))
                    else
                        deleteAddress.postValue(Resource.Error(it.exception.toString()))

                }

            } else
                deleteAddress.postValue(Resource.Error(addressResponse.exception.toString()))

        }
    }

    private fun getUser() {
        profile.postValue(Resource.Loading())
        firebaseDatabase.getUser().addSnapshotListener { value, error ->
            if (error != null)
                profile.postValue(Resource.Error(error.message))
            else
                profile.postValue(Resource.Success(value?.toObject(User::class.java)))

        }
    }

    fun uploadProfileImage(image: ByteArray) {
        uploadProfileImage.postValue(Resource.Loading())
        val name = UUID.nameUUIDFromBytes(image).toString()
        firebaseDatabase.uploadUserProfileImage(image, name).addOnCompleteListener {
            if (it.isSuccessful)
                uploadProfileImage.postValue(Resource.Success(name))
            else
                uploadProfileImage.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun updateInformation(firstName: String, lastName: String, email: String, imageName: String) {
        updateUserInformation.postValue(Resource.Loading())

        firebaseDatabase.getImageUrl(firstName, lastName, email, imageName) { user, exception ->

            if (exception != null)
                updateUserInformation.postValue(Resource.Error(exception))
                    .also { Log.d("test1", "up") }
            else
                user?.let {
                    onUpdateInformation(user).also { Log.d("test1", "down") }
                }
        }
    }

    private fun onUpdateInformation(user: User) {
        firebaseDatabase.updateUserInformation(user).addOnCompleteListener {
            if (it.isSuccessful)
                updateUserInformation.postValue(Resource.Success(user))
            else
                updateUserInformation.postValue(Resource.Error(it.exception.toString()))

        }
    }

    fun getUserOrders() {
        userOrders.postValue(Resource.Loading())
        firebaseDatabase.getUserOrders().addOnCompleteListener {
            if (it.isSuccessful)
                userOrders.postValue(Resource.Success(it.result?.toObjects(Order::class.java)))
            else
                userOrders.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun resetPassword(email: String) {
        passwordReset.postValue(Resource.Loading())
        firebaseDatabase.resetPassword(email).addOnCompleteListener {
            if (it.isSuccessful)
                passwordReset.postValue(Resource.Success(email))
            else
                passwordReset.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun getOrderAddressAndProducts(order: Order) {
        orderAddress.postValue(Resource.Loading())
        orderProducts.postValue(Resource.Loading())
        firebaseDatabase.getOrderAddressAndProducts(order, { address, aError ->
            if (aError != null)
                orderAddress.postValue(Resource.Error(aError))
            else
                orderAddress.postValue(Resource.Success(address))
        }, { products, pError ->

            if (pError != null)
                orderProducts.postValue(Resource.Error(pError))
            else
                orderProducts.postValue(Resource.Success(products))

        })
    }

    fun test(s: String) {
        Log.d("test", s)
    }

}