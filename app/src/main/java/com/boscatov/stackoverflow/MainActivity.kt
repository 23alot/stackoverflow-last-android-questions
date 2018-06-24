package com.boscatov.stackoverflow

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response
import android.net.ConnectivityManager
import android.os.Handler


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener{

    private val stackApi: StackApi = StackApi.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView(loadPage().questions)
        getStackPage(100, "desc", "activity", "android",
                "stackoverflow")
        swipe_layout.setOnRefreshListener(this)
    }
    private fun initRecyclerView(questions: ArrayList<Question>) {
            val adapter = PageAdapter(questions)
            page.adapter = adapter
            page.layoutManager = LinearLayoutManager(this)
    }
    private fun setQuestions(questions: ArrayList<Question>) {
        runOnUiThread {
            (page.adapter as PageAdapter).setItems(questions)
            page.invalidate()
        }
    }
    private fun getStackPage(pagesize: Int, order: String, sort: String, tagged: String, site: String) {
        var page: Response<StackPage>
        val th = Thread(Runnable {
            try {
                if(checkConnection()) {
                    page = stackApi.get(pagesize, order, sort, tagged, site).execute()
                    if(page.body() != null) {
                        val stackPage = page.body()!!
                        setQuestions(stackPage.questions)
                        cachePage(stackPage)
                    }
                }
                else {
                    userMessage(this.resources.getString(R.string.no_internet_connection))
                }
            }
            catch (e: RemoteException) {
                userMessage(this.resources.getString(R.string.connection_timeout))
            }
            catch(e: Exception) {
                userMessage(e.message)
            }
        })
        th.start()
    }
    private fun checkConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
    private fun userMessage(msg: String?) {
        runOnUiThread {
            user_message.text = msg
            user_message.visibility = View.VISIBLE
        }
    }
    private fun cachePage(page: StackPage) {
        val editor = this.getSharedPreferences("Page", Context.MODE_PRIVATE).edit()
        val gson = GsonBuilder().create()
        editor.putString("data", gson.toJson(page))
        editor.apply()
    }
    private fun loadPage(): StackPage {
        val gson = GsonBuilder().create()
        val sp = this.getSharedPreferences("Page", Context.MODE_PRIVATE)
        return gson.fromJson(sp.getString("data", null), StackPage::class.java) ?: StackPage(ArrayList())
    }
    override fun onRefresh() {
        Handler().postDelayed({
            getStackPage(100,
                    "desc", "activity", "android",
                    "stackoverflow")
            swipe_layout.isRefreshing = false
        }, 1000)

    }
}
