package com.franklinho.vidtrain_android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.common.io.Files;

import com.bumptech.glide.Glide;
import com.franklinho.vidtrain_android.R;
import com.franklinho.vidtrain_android.models.VidTrain;
import com.franklinho.vidtrain_android.models.Video;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VidTrainDetailActivity extends AppCompatActivity {
    @Bind(R.id.ivCollaborators) ImageView ivCollaborators;
//    @Bind(R.id.vvPreview) DynamicHeightVideoPlayerManagerView vvPreview;
    @Bind(R.id.videoView) VideoView videoView;
    @Bind(R.id.ibtnLike) ImageButton ibtnLike;
    @Bind(R.id.tvLikeCount) TextView tvLikeCount;
    @Bind(R.id.tvCommentCount) TextView tvCommentCount;
    @Bind(R.id.tvVideoCount) TextView tvVideoCount;

    Uri videoUri;
    public VidTrain vidTrain;
    private static final int VIDEO_CAPTURE = 101;

//    private VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
//        @Override
//        public void onPlayerItemChanged(MetaData metaData) {
//
//        }
//    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid_train_detail);
        ButterKnife.bind(this);

//        vvPreview.setHeightRatio(1);
//        videoView.setVisibility(View.INVISIBLE);
        String vidTrainObjectID = getIntent().getExtras().getString("vidTrain");
        ParseQuery<VidTrain> query = ParseQuery.getQuery("VidTrain");
        query.whereEqualTo("objectId", vidTrainObjectID);
        query.setLimit(1);
        query.findInBackground(new FindCallback<VidTrain>() {
            @Override
            public void done(List<VidTrain> objects, ParseException e) {
                if (e == null) {
                    vidTrain = objects.get(0);
                    String countString = String.format(getString(R.string.video_count),
                            vidTrain.getVideosCount());
                    tvVideoCount.setText(countString);

                    vidTrain.getUser().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            String profileImageUrl = (vidTrain.getUser()).getString(
                                    "profileImageUrl");
                            Glide.with(getBaseContext()).load(profileImageUrl).into(
                                    ivCollaborators);
                        }
                    });

                    final ParseFile videoFile = ((ParseFile) vidTrain.get("thumbnail"));
//                    videoView.setVideoURI(Uri.parse(videoFile.getUrl()));
//                    String url = "http://techslides.com/demos/sample-videos/small.mp4";
                    videoView.setVideoPath(videoFile.getUrl());
                    MediaController mediaController = new MediaController(getApplicationContext());
                    mediaController.setAnchorView(videoView);

//                    videoView.setVideoURI(Uri.parse(url));
                    videoView.setMediaController(mediaController);
                    videoView.requestFocus();
//                    videoView.start();
                    videoView.setOnPreparedListener(new OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            videoView.start();
                        }
                    });


//                    vvPreview.setHeightRatio(1);
//
//                    vvPreview.setVisibility(View.VISIBLE);
//                    vvPreview.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener() {
//                        @Override
//                        public void onVideoCompletionMainThread() {
//                            vvPreview.start();
//                        }
//                    });
//
//
//                    mVideoPlayerManager.playNewVideo(null, vvPreview, ((ParseFile) vidTrain.get("thumbnail")).getUrl());
                } else {
                    invalidVidTrain();
                }
            }
        });
    }

    public void invalidVidTrain() {
        Toast.makeText(getBaseContext(), "This VidTrain is invalid",
                Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public void showCreateFlow(View view) {
//        Toast.makeText(this, "Should navigate to creation flow", Toast.LENGTH_SHORT).show();

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            startCameraActivity();
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    public void startCameraActivity() {

        File mediaFile = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvidtrainvideo.mp4");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//        videoUri = getVideoFile(this).getAbsolutePath();
        videoUri = getOutputMediaFileUri();  // create a file to save the video
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri); ;
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                final Video video = new Video();
                File file = getOutputMediaFile();
                byte[] videoFileData;
                try {
                    videoFileData = Files.toByteArray(file);
                    final ParseFile parseFile = new ParseFile("video.mp4", videoFileData);
                    parseFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            video.setUser(ParseUser.getCurrentUser());
                            video.setVideoFile(parseFile);
                            video.setVidTrain(vidTrain);
                            video.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    ArrayList<Video> videos;
                                    vidTrain.setThumbnailFile(parseFile);

                                    if (vidTrain.get("videos") == null) {
                                        videos = new ArrayList<>();
                                    } else {
                                        videos = (ArrayList<Video>) vidTrain.get("videos");
                                    }
                                    videos.add(video);
                                    vidTrain.setVideos(videos);



                                    vidTrain.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            ArrayList<VidTrain> vidTrains;
                                            if (ParseUser.getCurrentUser().get("vidtrains") == null) {
                                                vidTrains = new ArrayList<>();

                                            } else {
                                                vidTrains = (ArrayList<VidTrain>) ParseUser.getCurrentUser().get("vidtrains");
                                            }
                                            if (!vidTrains.contains(vidTrain)) {
                                                vidTrains.add(vidTrain);
                                            }
                                            ParseUser.getCurrentUser().put("vidtrains", vidTrains);


                                            ArrayList<Video> videos;
                                            if (ParseUser.getCurrentUser().get("videos") == null) {
                                                videos = new ArrayList<>();
                                            } else {
                                                videos = (ArrayList<Video>) ParseUser.getCurrentUser().get("vidtrains");
                                            }
                                            videos.add(video);
                                            ParseUser.getCurrentUser().put("videos", vidTrains);

                                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    successfullyAddedVideo();
                                                }
                                            });


                                        }
                                    });

                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",  Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",  Toast.LENGTH_LONG).show();
            }
        }

    }

    public void successfullyAddedVideo() {
        Toast.makeText(getBaseContext(), "Successfully added video",
                Toast.LENGTH_SHORT).show();

    }

    public File getVideoFile(Context context) {
//        return new File(context.getExternalFilesDir(null), APP_TAG+"/"+videoFileName);
        return new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvidtrainvideo.mp4");
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri()
    {
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "VidTrainApp");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("VidTrainApp", "failed to create directory");
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "VID_CAPTURED" + ".mp4");
        return mediaFile;
    }
}
