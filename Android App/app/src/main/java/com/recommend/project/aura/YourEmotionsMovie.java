package com.recommend.project.aura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class YourEmotionsMovie extends AppCompatActivity {

    String movieList[] = new String[5];

    String happyMovies[] = {"The Avengers", "The Lion King", "Back to the Future", "The Wolf of Wall Street", "How to Train Your Dragon"};
    String[] neutralMovies={"The Silence of the Lambs","The Godfather","Inception","The Exorcist","Batman Begins"};
    String angryMovies[]={"Jaws","Star Wars: Episode V - The Empire Strikes Back","Fight Club","Die Hard","Deadpool"};
    String surpriseMovies[]={"The Shining","The Matrix","Million Dollar Baby","Shutter Island","Toy Story"};
    String sadMovies[]={"Inside Out","2001: A Space Odyssey","Dead Poets Society","Harry Potter and the Deathly Hallows: Part 2","La La Land"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_emotions_movie);

    }

    public void makeItTinted(View view) {
//        if ((((ColorDrawable)view.getBackground()).getColor()) == Color.GRAY ){
//            view.setBackgroundColor(Color.BLACK);
//            Toast.makeText(getApplicationContext(),"here inside",Toast.LENGTH_SHORT).show();
//
//        } else {
//            view.setBackgroundColor(Color.GRAY);
//        }
//        Toast.makeText(getApplicationContext(),String.valueOf(((ColorDrawable)view.getBackground()).getColor()),Toast.LENGTH_SHORT).show();

        view.setBackgroundColor(Color.GRAY);
        String str = view.getResources().getResourceEntryName(view.getId());
        String strNum = str.substring(str.length() - 2);
        int n = Integer.parseInt(strNum);


        if(n>=01 && n<=05){
            movieList[0] = happyMovies[n-1];
        } else if(n>=06 && n<=10){
            n = n % 5;
            if (n == 0)
                n = 5;
            movieList[1] = neutralMovies[n-1];
        } else if(n>=11 && n<=15){
            n = n % 5;
            if (n == 0)
                n = 5;
            movieList[2] = angryMovies[n-1];
        } else if(n>=16 && n<=20){
            n = n % 5;
            if (n == 0)
                n = 5;
            movieList[3] = surpriseMovies[n-1];
        } else {
            n = n % 5;
            if (n == 0)
                n = 5;
            movieList[4] = sadMovies[n-1];
        }

        Button nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                for (String element : movieList) {
                    if (element == null) {
                        flag = 1;
//                        Toast.makeText(getApplicationContext(), "here inside", Toast.LENGTH_SHORT).show();
                        break;
                    }
//                    else{
//                        Toast.makeText(getApplicationContext(), "Please select for all emotions", Toast.LENGTH_SHORT).show();
//                    }
                }
                if (flag == 0)
                    saveSP();
                }

        });

//        Toast.makeText(getApplicationContext(),movieList[0]+", "+movieList[1]+", "+movieList[2]+", "+movieList[3]+", "+movieList[4]+", ",Toast.LENGTH_SHORT).show();
    }


    private void saveSP() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("EmoMoviesList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Happy", movieList[0]);
        editor.putString("Neutral", movieList[1]);
        editor.putString("Angry", movieList[2]);
        editor.putString("Surprise", movieList[3]);
        editor.putString("Sad", movieList[4]);
        editor.apply();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

//        SharedPreferences sharedPreferences = getSharedPreferences("EmoMoviesList", Context.MODE_PRIVATE);
//        String success = sharedPreferences.getString("happyMovie", null);
//        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT).show();
    }
}