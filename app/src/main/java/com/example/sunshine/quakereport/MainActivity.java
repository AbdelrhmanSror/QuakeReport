package com.example.sunshine.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements getItem, LoaderManager.LoaderCallbacks<ArrayList<information>> {
TextView internet;
ProgressBar mprogress;

    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
wordAdapter list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internet=findViewById(R.id.internetConnection);
         mprogress=findViewById(R.id.loading_spinner);

        //checking if there is internet connection or not
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected)
        {
            mprogress.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(0, null, this).forceLoad();//if connected then start loader

        }else
        {
            internet.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onitemclick() {
        String url = QueryUtils.geturl;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

    }
    public void updateUi(ArrayList<information>data)
    {
        RecyclerView mrecyclerView=findViewById(R.id.list);
        TextView stateText=findViewById(R.id.EarthquakeText);
        if(data.isEmpty())//if there is no data then show on screen that there is no data
        {
            mrecyclerView.setVisibility(View.GONE);
            stateText.setVisibility(View.VISIBLE);
            list = new wordAdapter(data, this);

            // DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            mrecyclerView.setAdapter(list);
            // mrecyclerView.addItemDecoration(itemDecorator);

            mrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        else {
            mrecyclerView.setVisibility(View.VISIBLE);
            stateText.setVisibility(View.GONE);
            list = new wordAdapter(data, this);

            // DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            mrecyclerView.setAdapter(list);
            // mrecyclerView.addItemDecoration(itemDecorator);

            mrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

    }

    @NonNull
    @Override
    public Loader<ArrayList<information>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new earthquakereportLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<information>> loader, ArrayList<information> information)
    {
        mprogress=findViewById(R.id.loading_spinner);
        if(information!=null &&!information.isEmpty())
            updateUi(information);
        mprogress.setVisibility(View.GONE);//make progress bar get disspapered when the data is fetched


    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<information>> loader) {
        updateUi(new ArrayList<information>());

    }


    public  class wordAdapter extends RecyclerView.Adapter<wordAdapter.viewHolder> {
        ArrayList<information>data;
        public  View GetItemView;
        public getItem c;

        public wordAdapter(ArrayList<information>data,getItem c)
        {
            this.data=data;
            this.c=c;
        }



        public class viewHolder extends RecyclerView.ViewHolder  {
            public TextView number;
            public TextView city;
            public TextView city1;
            public TextView date;
            public viewHolder(View itemView) {
                super(itemView);
                number= itemView.findViewById(R.id.number);
                city=itemView.findViewById(R.id.city);
                city1=itemView.findViewById(R.id.city1);
                date=itemView.findViewById(R.id.date);
               GetItemView=itemView;
               itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       c.onitemclick();
                   }
               });


            }


        }

        @Override
        public wordAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View create_a_view=getLayoutInflater().from(getApplicationContext()).inflate(R.layout.viewholder,parent,false);
            viewHolder assignViews= new viewHolder(create_a_view);

            return assignViews;
        }

        @Override
        public void onBindViewHolder(wordAdapter.viewHolder holder, int position) {
            information Assign_data=data.get(position);
            holder.number.setText(Assign_data.getNumber());
            holder.date.setText(Assign_data.getdate());
            holder.city.setText(Assign_data.getCity());
            holder.city1.setText(Assign_data.getCity1());
           // Drawable x=holder.number.getBackground(); i may need this in future to get the type of background drawable shape or gradient shape etc...
            // but because i know the type then i do not need it now.
            /**
             * example
             *  Drawable background = imageViewStep1.getBackground();
             *         if (background instanceof ShapeDrawable) {
             *             // cast to 'ShapeDrawable'
             *             ShapeDrawable shapeDrawable = (ShapeDrawable) background;
             *             shapeDrawable.getPaint().setColor(ContextCompat.getColor(this, R.color.md_blue_500));
             *         } else if (background instanceof GradientDrawable) {
             *             // cast to 'GradientDrawable'
             *             GradientDrawable gradientDrawable = (GradientDrawable) background;
             *             gradientDrawable.setColor(ContextCompat.getColor(this, R.color.md_blue_500));
             *         } else if (background instanceof ColorDrawable) {
             *             // alpha value may need to be set again after this call
             *             ColorDrawable colorDrawable = (ColorDrawable) background;
             *             colorDrawable.setColor(ContextCompat.getColor(this, R.color.md_blue_500));
             *         }
             * */

            GradientDrawable z= (GradientDrawable) holder.number.getBackground();
            if(Double.valueOf(Assign_data.getNumber())<2)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude1));
            }
            else if(Double.valueOf(Assign_data.getNumber())>2&&Double.valueOf(Assign_data.getNumber())<3)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude2));
            }
            else if(Double.valueOf(Assign_data.getNumber())>3&&Double.valueOf(Assign_data.getNumber())<4)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude3));

            }else if(Double.valueOf(Assign_data.getNumber())>4&&Double.valueOf(Assign_data.getNumber())<5)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude4));

            }else if(Double.valueOf(Assign_data.getNumber())>5&&Double.valueOf(Assign_data.getNumber())<6)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude5));

            }else if(Double.valueOf(Assign_data.getNumber())>6&&Double.valueOf(Assign_data.getNumber())<7)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude6));

            }else if(Double.valueOf(Assign_data.getNumber())>7&&Double.valueOf(Assign_data.getNumber())<8)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude7));

            }else if(Double.valueOf(Assign_data.getNumber())>8&&Double.valueOf(Assign_data.getNumber())<9)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude8));

            }else if(Double.valueOf(Assign_data.getNumber())>9&&Double.valueOf(Assign_data.getNumber())<10)
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude9));

            }
            else
            {
                z.setColor(ContextCompat.getColor(getApplicationContext(), R.color.magnitude10plus));
            }



        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    }
    private static class earthquakereportLoader extends AsyncTaskLoader<ArrayList<information>>
    {
        public earthquakereportLoader(Context c)
        {
            super(c);
        }
        public ArrayList<information> loadInBackground() {
            if(SAMPLE_JSON_RESPONSE.length()<1||SAMPLE_JSON_RESPONSE==null)
            {
                return null;
            }
            return  QueryUtils.Fetch_earthquake(SAMPLE_JSON_RESPONSE);
        }
    }


   /* private  class earthquakereportAsync extends AsyncTask<String,Void,ArrayList<information>>
    {


        @Override
        protected ArrayList<information> doInBackground(String... strings) {
            if(strings[0].length()<1||strings[0]==null)
            {
                return null;
            }
       return  QueryUtils.Fetch_earthquake(strings[0])     ;   }

        @Override
        protected void onPostExecute(ArrayList<information> information)
        {
            list=null;
            if(information!=null &&!information.isEmpty())
            updateUi(information);

        }


    }*/


}
