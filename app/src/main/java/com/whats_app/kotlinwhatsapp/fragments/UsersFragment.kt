package com.whats_app.kotlinwhatsapp.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.whats_app.kotlinwhatsapp.R
import com.whats_app.kotlinwhatsapp.adapters.UserAdapter
import kotlinx.android.synthetic.main.fragment_users_fragments.*


class UsersFragment : Fragment() {

    // variables
    var mUserDatabase: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_users_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        Log.i("Message", "mUserDatabase : " + mUserDatabase)

        users_recycler_view.setHasFixedSize(true)
        users_recycler_view.layoutManager = linearLayoutManager
        users_recycler_view.adapter = UserAdapter(mUserDatabase!!, context!!)
    }
}
