package com.example.loskate0;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;


public class CreateSpot_Fragment extends Fragment {
    /************************************************/
    // Editable Variables
    /************************************************/
    EditText m_Title;
    EditText m_Notes;
    ImageButton m_PictureButton;
    Button m_SubmitButton;
    /************************************************/
    // Final Variables after user input/changes them
    String m_UpdatedTitle;
    String m_UpdatedNotes;
    Bitmap m_UpdatedImage;
    Boolean imageClicked = false;
    String ID;
    /************************************************/
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_spot_, container, false);

        Toast.makeText(CreateSpot_Fragment.this.getContext(), "Detail Spot Info", Toast.LENGTH_SHORT).show();

        InitializeThePage(v);

        //Submit Button On Click Method
        m_SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_UpdatedTitle = m_Title.getText().toString();
                m_UpdatedNotes = m_Notes.getText().toString();

                m_PictureButton.buildDrawingCache();
                m_UpdatedImage = m_PictureButton.getDrawingCache();

                Log.d("Pagnozzi", "Updated image = " + m_UpdatedImage);
                Log.d("Pagnozzi", "imageClicked = " + imageClicked);


                if(imageClicked == true) {
                    UpdateDatabaseObject(ID, m_UpdatedTitle, m_UpdatedNotes, m_UpdatedImage);
                    BackToMap();
                }
                else
                {
                    UpdateDatabaseObject(ID, m_UpdatedTitle, m_UpdatedNotes, null);
                    BackToMap();
                }

            }
        });

        // Image Button On Click Method
        m_PictureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
                imageClicked = true;
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        m_UpdatedImage = bitmap;
        m_PictureButton.setImageBitmap(m_UpdatedImage);
    }

    private void InitializeThePage(View v)
    {

        m_Title = v.findViewById(R.id.Edit_Spot);
        m_Title.setTextColor(Color.argb(255,255,165,0));
        m_Title.setText("Enter Title...");

        m_Notes = v.findViewById(R.id.Edit_Notes);
        m_Notes.setTextColor(Color.argb(255,255,165,0));
        m_Notes.setText("Enter Notes...");

        m_SubmitButton = v.findViewById(R.id.Submit_Button);
        m_SubmitButton.setTextColor(Color.WHITE);


        m_PictureButton = v.findViewById(R.id.Edit_Image);


        Bundle bun = getArguments();
        if(bun != null)
        {
            String id = bun.getString("d");
            ID = id;
        }
        else
            m_Title.setText("Something went Wrong ! ");
    }

    private void UpdateDatabaseObject(String id, String title, String notes, Bitmap image)
    {

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("spots").child(id).child("id").setValue(title);
        mDatabase.child("spots").child(id).child("notes").setValue(notes);
        // New Way
        mDatabase.child("Users").child(userID).child(id).child("id").setValue(title);
        mDatabase.child("Users").child(userID).child(id).child("notes").setValue(notes);


        if(image!=null) {
            String bmString; // bitmap String
            bmString = EncodeBitmap(image, Bitmap.CompressFormat.JPEG, 100);
            mDatabase.child("spots").child(id).child("image").setValue(bmString);
            // New Way
            mDatabase.child("Users").child(userID).child(id).child("image").setValue(bmString);

        }
        else {
             mDatabase.child("spots").child(id).child("image").setValue(null);
            // New Way
            mDatabase.child("Users").child(userID).child(id).child("image").setValue(null);
        }
    }

    private void BackToMap()
    {
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.Frag_Trans_Map();
    }

    public String EncodeBitmap(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
