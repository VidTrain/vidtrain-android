# Vidtrain

Setup instructions: 

You need to use the debug.keystore in this repository for the Google Maps API calls to succeed. 
Copy debug.keystore from this repo into ~/.android/ (on Mac) or C:\Users\.android\ (on Windows).
Reference: http://stackoverflow.com/questions/9653882/how-to-develop-an-android-app-with-google-maps-api-on-multiple-computers/9653946#9653946 and http://guides.codepath.com/android/Google-Maps-Fragment-Guide. 

## About the app

VidTrain (Placeholder Name) is a collaborative video sharing app. A user can create new stories that have a location associated with them and then invite collaborators to add additional videos.

Other users can comment on the videos with annotations to particular points in the video.

* User Stories
	*  :white_check_mark: User should be able to log in with facebook
	*  :white_check_mark: User should be able to see map screen with locations of videos
		*  :white_check_mark: Map markers should show video preview when tapped on
		*  :white_check_mark: When tapped, video preview should go to story detail 
	*  :white_large_square: User should be able to see a list of all the local videos
	*  :white_check_mark: User should be able to see a list of all of the currently popular videos
	*  :white_check_mark: User should be able to see a user's profile
		*  :white_check_mark: Profile should include stream of all the videos they're collaborating on
		*  :white_large_square: Profile should also show username, post count, and point count
	*  :white_check_mark: Story Detail Screen should show the following
		*  :white_check_mark:  Video player
		*  :white_large_square:  Colored video snippet location
		*  :white_check_mark:  Collaborators
		*  :white_large_square: Collaborator commentary overlay
		*  :white_large_square:  Comment markers on video time bar
		*  :white_check_mark:  Video points
		*  :white_large_square:  Comment Count
		*  :white_large_square:  Stream of comments
		*  :white_large_square:  Capability to add comment
			*  :white_large_square: Comments should be inserted chronologically by video
		*  :white_large_square: Collaborator should be able to add video to story
	*  :white_check_mark:  User should be able to create new video story on the Map, List, or Popular Pages
		*  :white_large_square: Video should be hold to record
		*  :white_check_mark: Video should have time limit
		*  :white_check_mark: After recording video, user should be able to add details to story:
			*  :white_check_mark: Title
			*  :white_check_mark: Collaborators can be added
				*  :white_check_mark: autocomplete of user's friends
			*  :white_check_mark: Story can be set to public so anyone can collaborate
			*  :white_large_square: Story privacy can be set to public or private. If private, only viewable by collaborators

[![Video Walkthrough](https://github.com/VidTrain/vidtrain-android/blob/master/VideoScreenshot.png)](http://www.youtube.com/watch?v=VfsNuAMRq7o "VidTrain Android Walkthrough")

Note: Videos are sideways in simulator, but on devices are oriented correctly
![Core Functionality](https://github.com/VidTrain/vidtrain-android/blob/master/VidTrainWalkThroughCoreFlowsMaterial.gif)


![Creation Detail](https://github.com/VidTrain/vidtrain-android/blob/master/activities-LogInActivity-03142016180140.png)

![Collaborator Autocomplete](https://github.com/VidTrain/vidtrain-android/blob/master/activities-LogInActivity-03142016180207.png)
