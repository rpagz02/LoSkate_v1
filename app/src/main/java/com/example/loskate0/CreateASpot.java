package com.example.loskate0;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateASpot extends Fragment
{
    /************************************************/
    // Editable Variables
    /************************************************/
    EditText m_Title;
    EditText m_Notes;
    ImageButton m_Picuture;
    Button m_SubmitButton;
    /************************************************/
    // Final Variables after user input/changes them
    String m_UpdatedTitle;
    String m_UpdatedNotes;
    String ID;
    /************************************************/
    DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_spot, container, false);

        Toast.makeText(CreateASpot.this.getContext(), "Detail Spot Info", Toast.LENGTH_SHORT).show();

        InitializeThePage(v);

        m_SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            m_UpdatedTitle = m_Title.getText().toString();
            m_UpdatedNotes = m_Notes.getText().toString();
            UpdateDatabaseObject(ID, m_UpdatedTitle, m_UpdatedNotes);
            BackToMap();
            }
        });

        return v;
    }

    private void InitializeThePage(View v)
    {
        m_Title = v.findViewById(R.id.Title);
        m_Title.setTextColor(Color.BLACK);

        m_Notes = v.findViewById(R.id.Notes);
        m_Notes.setTextColor(Color.BLACK);

        m_Picuture = v.findViewById(R.id.ImageButton);

        m_SubmitButton = v.findViewById(R.id.SubmitButton);
        m_SubmitButton.setTextColor(Color.WHITE);


        Bundle bun = getArguments();
        if(bun != null)
        {
            String id = bun.getString("d");
            ID = id;
        }
        else
            m_Title.setText("Something went Wrong ! ");
    }

    private void UpdateDatabaseObject(String id, String title, String notes)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("spots").child(id).child("id").setValue(title);
        mDatabase.child("spots").child(id).child("notes").setValue(notes);

    }

    private void BackToMap()
    {
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.Frag_Trans_Map();
    }
}
