package ui;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourself.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firestore.v1.Precondition;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

import model.Journal;
import util.JournalAPI;

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.ViewHolder> {
    private List<Journal> journalList;
    private Context context;
    private FirebaseAuth firebaseAuth;
   private FirebaseUser user;

    public JournalRecyclerAdapter(List<Journal> journalList, Context context) {
        this.journalList = journalList;
        this.context = context;
    }

    @NonNull
    @Override
    public JournalRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.journal_row, parent, false);


        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerAdapter.ViewHolder holder, int position) {
        Journal journal=journalList.get(position);
        String imageUrl;
        holder.title.setText(journal.getTitle());
        holder.thoughts.setText(journal.getThoughts());
        holder.title.setText(journal.getTitle());
        holder.name.setText(journal.getUserName());
      String timeAgo= (String) DateUtils.getRelativeTimeSpanString(journal.getTimeAdded().getSeconds()*1000);
        imageUrl=journal.getImageUrl();
        holder.dateAdded.setText(timeAgo);
        Picasso.get().load(imageUrl).placeholder(R.drawable.image1)

                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title,thoughts,dateAdded,name;
        public ImageView image;
        public ImageButton shareButton;
        public Button deleteButton;

        String userId,userName;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            title= itemView.findViewById(R.id.journal_title_list);
            thoughts=itemView.findViewById(R.id.journal_thoughts_list);


            dateAdded=itemView.findViewById(R.id.journal_timestamp_list);
            image=itemView.findViewById(R.id.journal_image_list);
            shareButton=itemView.findViewById(R.id.journal_row_share_button);
            name=itemView.findViewById(R.id.journal_row_username);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                String shareTitkle= (String) title.getText();
                String sgareThougts= (String) thoughts.getText();
                String message="Hi , I am using the self app, here is what I wrote today"+"\n"+shareTitkle+"\n"+sgareThougts;
                    Intent intent=new Intent(Intent.ACTION_SEND);

                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,message);

                           context.startActivity(intent);

                }
            });


        }
    }


}
