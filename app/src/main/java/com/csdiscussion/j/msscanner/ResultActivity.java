package com.csdiscussion.j.msscanner;

import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

public class ResultActivity extends AppCompatActivity {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    TextView EmptyResults;
    Document doc, doc1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Results");
        setSupportActionBar(toolbar);

        Bundle barcodeData = getIntent().getExtras();
        if (barcodeData == null) return;
        final String query = barcodeData.getString("myResult");
        EmptyResults = findViewById(R.id.list_empty);
        EmptyResults.setVisibility(View.GONE);
        new ScrapeGoogleTask().execute(query);
    }

    private class ScrapeGoogleTask extends AsyncTask<String, Void, Void> {
        boolean ConnectionError = false;
        protected Void doInBackground(String... query) {
            doc = doc1 =  Jsoup.parse("");
            try {
                Log.v("diBG", query[0]);
                doc = Jsoup.connect("https://www.google.com/search?q=" + query[0] + "%20filetype:pdf&filter=0").userAgent(USER_AGENT).ignoreHttpErrors(false).get();
                doc1 = Jsoup.connect("https://www.google.com/search?q=" + query[0] + "%20filetype:pdf%20%20site:physicsandmathstutor.com&filter=0").userAgent(USER_AGENT).get();

            } catch (Exception e) {
                e.printStackTrace();
                ConnectionError = true;
            }
            return null;
        }

        protected void onPostExecute(Void Result) {
            boolean PMTflag;
            ArrayList<result> resultArrayList = new ArrayList<>();
            Document Analysedoc = doc;
            if (ConnectionError) Snackbar.make(findViewById(R.id.resultView), "Error, check your internet. ", Snackbar.LENGTH_INDEFINITE).show();
            do {
                PMTflag = Analysedoc == doc1;
                for (Element result : Analysedoc.select("div.r a:nth-child(2)")) {

                    final String title = result.select("h3").text();
                    final String qpurl = result.attr("href");
                    String msurl = "N/A";
                    Log.v(title,qpurl);
                    if (qpurl.contains("qp")) msurl = qpurl.replace("qp", "ms");
                    if (qpurl.contains("QP")) msurl = qpurl.replace("QP", "MS");
                    if (qpurl.contains("que")) msurl = qpurl.replace("que", "msc");
                    if (qpurl.contains("QUE")) msurl = qpurl.replace("QUE", "MSC");
                    if (qpurl.contains("question-paper"))
                        msurl = qpurl.replace("question-paper", "mark-scheme");
                    if (qpurl.contains("QUESTION-PAPER"))
                        msurl = qpurl.replace("QUESTION-PAPER", "MARK-SCHEME");
                    if (qpurl.contains("question_paper"))
                        msurl = qpurl.replace("question_paper", "mark_scheme");
                    if (qpurl.contains("QUESTION_PAPER"))
                        msurl = qpurl.replace("QUESTION_PAPER", "MARK_SCHEME");
                    if (qpurl.contains("physicsandmathstutor.com")) PMTflag = true;
                    if (!msurl.equals("N/A") && !title.equals(" ")) {
                        resultArrayList.add(new result(title, msurl, qpurl));
                    }
                    //Log.v("msurl: ", msurl);

                }
                Analysedoc = PMTflag?doc:doc1;
            }while (!PMTflag);
            //resultArrayList.add(new result(getIntent().getExtras().getString("myResult"), "http://msurl", "http://qpurl"));
            ListAdapter adapter = new CustomAdaptor(ResultActivity.this, resultArrayList);
            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(adapter);
            EmptyResults.setVisibility((adapter.isEmpty())? View.VISIBLE: View.GONE);
        }
    }


}
