package com.example.potashin.ui.gallery

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.potashin.R
import com.example.potashin.data.Api
import com.example.potashin.data.model.Transaction
import com.example.potashin.data.model.Wallet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        //        val arguments = arguments
        val id = arguments?.getString("wallet")
        val user = arguments?.getString("user")
        val token = arguments?.getString("token")

        println("create view WALLET ${id}")

        val walletAmount: TextView = root.findViewById(R.id.wallet_balance)
        val walletNumber: TextView = root.findViewById(R.id.wallet_number)

        val api = Api()
        GlobalScope.launch {
            val result = token?.let { api.sendGetRequest("wallets/wallet/${id}", it) };
            println("RESULT $result")

            val  wallet = Gson().fromJson(result, Wallet::class.java)
            println("LOADED WALLET ${wallet.number}: ${wallet.amount}")

            walletAmount.text = "Balance" + wallet.amount.toString()
            walletNumber.text = "Number: " + wallet.number

            activity?.title = wallet.name
        }

//        val textView: TextView = root.findViewById(R.id.text_gallery)
//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        listView = root.findViewById<ListView>(R.id.transactions_list_view)

        GlobalScope.launch {
            val result = token?.let { api.sendGetRequest("transactions/get/${id}", it) };
            println("RESULT $result")

            val itemType = object : TypeToken<List<Transaction>>() {}.type
            val transactions = Gson().fromJson<List<Transaction>>(result, itemType)

            activity?.runOnUiThread(Runnable {
                val listItems = arrayOfNulls<String>(transactions.size)
                for (i in 0 until transactions.size) {
                    listItems[i] = transactions[i].toStringInList();
                }

                val adapter = ArrayAdapter(root.context, android.R.layout.simple_list_item_1, listItems)
                listView.adapter = adapter
            })
        }

        return root
    }
}
