package com.example.scavengerhuntapp.player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scavengerhuntapp.shared.MainActivity;
import com.example.scavengerhuntapp.R;
import com.example.scavengerhuntapp.objects.Challenge;
import com.example.scavengerhuntapp.objects.Hunt;
import com.example.scavengerhuntapp.objects.Submission;
import com.example.scavengerhuntapp.objects.Team;
import com.example.scavengerhuntapp.objects.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class PlayerHuntLandingActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private ListView challengesListView;
    private SwipeRefreshLayout swipeContainer;

    private List<Challenge> challengesList;
    private CustomAdapter huntNamesArray;

    private String huntID;
    private String huntName;
    private String teamName;
    private String teamID;

    private String TAG = "PlayerHuntLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_hunt_landing);

        db = FirebaseFirestore.getInstance();

        challengesListView = findViewById(R.id.challenge_list);
        swipeContainer = findViewById(R.id.swipe_container_challenges);

        challengesList = new ArrayList<>();
        huntNamesArray = new CustomAdapter();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);
        teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);

        this.setTitle("Hunt: " + huntName);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createCurrChallListView();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_alerts:
                        Intent intent = new Intent(PlayerHuntLandingActivity.this, AnnouncementsActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                        intent.putExtra(Team.KEY_TEAM_ID, teamID);
                        startActivity(intent);
                        break;
                    case R.id.action_challenges:
                        break;
                    case R.id.action_team:
                        Intent intent1 = new Intent (PlayerHuntLandingActivity.this, PlayerViewTeamActivity.class);
                        intent1.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent1.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent1.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent1.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        intent1.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                        startActivity(intent1);
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(PlayerHuntLandingActivity.this, RankingsActivity.class);
                        intent3.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        intent3.putExtra(Team.KEY_TEAM_NAME, teamName);
                        intent3.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                        intent3.putExtra(Team.KEY_TEAM_ID, teamID);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        createCurrChallListView();
    }

    private void createCurrChallListView(){
        challengesList.clear();

        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);
        teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);

        Log.w(TAG, "Given huntID: " + huntID);
        Log.w(TAG, "Given teamID: " + teamID);

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).document(teamID).collection(Challenge.KEY_CHALLENGES).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots){
                        Log.w(TAG, Integer.toString(queryDocumentSnapshots.size()));
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            challengesList.add(documentSnapshot.toObject(Challenge.class));
                            Log.w(TAG, documentSnapshot.getId());
                        }
                        challengesListView.setAdapter(huntNamesArray);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return challengesList.size();
        }

        @Override
        public Object getItem(int i) {
            return challengesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.challenge_custom_view, null);
            Button submitChallenge = view.findViewById(R.id.submitButtonChallenge);

            TextView challengeText = view.findViewById(R.id.challengeStateText);
            challengeText.setVisibility(View.VISIBLE);

            if (challengesList.get(i).getState().equals(Challenge.KEY_IN_REVIEW)){
                submitChallenge.setVisibility(View.INVISIBLE);
                challengeText.setText("In review.");
                view.setBackgroundColor(Color.parseColor("#8Ceaab00")); // yellow color
            } else if (challengesList.get(i).getState().equals(Challenge.KEY_REJECTED)){
                submitChallenge.setVisibility(View.VISIBLE);
                challengeText.setText("Rejected.");
                view.setBackgroundColor(Color.parseColor("#8CB1040E"));
            } else if (challengesList.get(i).getState().equals(Challenge.KEY_ACCEPTED)){
                submitChallenge.setVisibility(View.INVISIBLE);
                challengeText.setText("Approved!");
                view.setBackgroundColor(Color.parseColor("#8C009b76")); // green color
            } else {
                submitChallenge.setVisibility(View.VISIBLE);
                challengeText.setVisibility(View.GONE);
            }

            // Populate list view
            ImageView imageView = view.findViewById(R.id.iconImageView);
            TextView challengeTextView = view.findViewById(R.id.challengeTextView);
            TextView challengeLocationTextView = view.findViewById(R.id.challengeLocationTextView);
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            TextView points = view.findViewById(R.id.challengePoints);

            submitChallenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayerHuntLandingActivity.this, SubmitChallengeActivity.class);
                    intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                    intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                    intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                    intent.putExtra(Team.KEY_TEAM_ID, teamID);
                    intent.putExtra(Challenge.KEY_CHALLENGE_ID, challengesList.get(i).getChallengeID());
                    intent.putExtra(Submission.KEY_DESCRIPTION, challengesList.get(i).getDescription());
                    intent.putExtra(Submission.KEY_LOCATION, challengesList.get(i).getLocation());
                    intent.putExtra(Submission.KEY_POINTS, String.valueOf((challengesList.get(i).getPoints())));
                    intent.putExtra(Submission.KEY_ICON, String.valueOf(challengesList.get(i).getIcon()));
                    startActivity(intent);
                }
            });

            imageView.setImageResource(challengesList.get(i).getIcon());
            challengeTextView.setText(challengesList.get(i).getDescription());
            challengeLocationTextView.setText(challengesList.get(i).getLocation());
            points.setText(challengesList.get(i).getPoints() + " Pts");
            checkBox.setVisibility(View.GONE);

            return view;
        }
    }

    // TODO: COPY THIS TO ALL PAGES THAT ARE PAST THE LANDING
    // Note: No xml changes are required

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_hunts:
                Intent intent = new Intent(PlayerHuntLandingActivity.this, PlayerLandingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(PlayerHuntLandingActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // End of top menu code
}