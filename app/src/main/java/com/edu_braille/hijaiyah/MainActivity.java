package com.edu_braille.hijaiyah;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRecord = (Button)findViewById(R.id.btnRecord);
        Button btn=(Button)findViewById(R.id.btnRekam);

        btn.setOnClickListener(op);
        btnRecord.setOnClickListener(op);
    }

    private ArrayList<Double> al;

    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case(R.id.btnRekam):
                    getAmplitude();
                    writeDataText();
                    break;
            }
        }
    };

    private void getAmplitude()
    {
        al=new ArrayList<>();

        try
        {
            String path=  Environment.getExternalStorageDirectory().getAbsolutePath()+"/audio.wav";
            WavFile wavFile = WavFile.openWavFile(new File(path));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            // Create a buffer of 100 frames
            double[] buffer = new double[100 * numChannels];

            int framesRead;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            int ctr=0;
            do
            {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, 100);

                // Loop through frames and look for minimum and maximum value

                for (int s=0 ; s<framesRead * numChannels ; s++)
                {
                    ctr++;
                    if (al.size()<300)
                    {
                        al.add(buffer[s]);
                    }
                    //System.out.println(buffer[s]);
                    if (buffer[s] > max) max = buffer[s];
                    if (buffer[s] < min) min = buffer[s];
                }
            }
            while (framesRead != 0);

            // Close the wavFile
            wavFile.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void writeDataText(){
        String dataTulis="";
        for (int i=0;i<al.size();i++)
        {
            dataTulis=dataTulis+al.get(i)+"\n";
        }

        try {
            File myFile = new File( Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(dataTulis);
            myOutWriter.close();
            fOut.close();
            Toast.makeText(getBaseContext(),
                    "Done writing SD 'mysdfile.txt'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
