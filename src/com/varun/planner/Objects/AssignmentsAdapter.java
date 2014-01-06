package com.varun.planner.Objects;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.varun.planner.R;

public class AssignmentsAdapter extends ArrayAdapter<Assignment>
{

	int resource;
	String response;
	int count;
	static TextView secondAss;

	public AssignmentsAdapter(Context context, int resource,
			List<Assignment> Assignments)
	{
		super(context, resource, Assignments);
		this.resource = resource;
		count = 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		Typeface thin = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Roboto-Thin.ttf");
		Typeface Black = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Roboto-Black.ttf");
		LinearLayout AssView;
		// Get the current alert object
		Assignment Ass = getItem(position);
		// Inflate the view
		if (convertView == null)
		{
			AssView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, AssView, true);
		}

		else

		{
			AssView = (LinearLayout) convertView;
		}
		TextView Class = (TextView) AssView.findViewById(R.id.ClassHeader);
		Class.setVisibility(View.VISIBLE);
		if (position > 0)
		{
			Assignment prevAss = getItem(position - 1);
			if (prevAss.getAssClass().equals(Ass.getAssClass()))
			{
				Class.setVisibility(View.GONE);
			}
			else
			{
				Class.setText(Ass.getAssClass());
			}

		}
		else
		{
			Class.setText(Ass.getAssClass());
		}

		// Get the text boxes from the listitem.xml file

		TextView Title = (TextView) AssView.findViewById(R.id.TitleHeader);
		TextView DueDate = (TextView) AssView
				.findViewById(R.id.DueDateSubheader);
		// Assign the appropriate data from our assignment object above

		Class.setTypeface(thin);
		Title.setTypeface(Black);

		String date = Ass.formatCal();
		// Class.setText(Ass.getAssClass());
		Title.setText(Ass.getTitle());
		DueDate.setText(date);

		if (Ass.isUrgent())
		{
			int red = Color.parseColor("#FF4444");
			Title.setTextColor(red);
			DueDate.setTextColor(red);
		}
		else
		{
			int black = Color.BLACK;
			Title.setTextColor(black);
			DueDate.setTextColor(black);
		}
		Button playbutton = (Button) AssView.findViewById(R.id.PlayButton);
		if(Ass.hasAudio())
		{
			final String path = Ass.getAudioPath();
			playbutton.setVisibility(View.VISIBLE);
			playbutton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					MediaPlayer mPlayer = new MediaPlayer();
			        try {
			            mPlayer.setDataSource(path);
			            mPlayer.prepare();
			            mPlayer.start();
			        } catch (IOException e) {
			            Log.e("Audio Player", "prepare() failed");
			        }
			    }
					
				
			});
		}
		else
			playbutton.setVisibility(View.GONE);
		
		
		return AssView;
	}

}
