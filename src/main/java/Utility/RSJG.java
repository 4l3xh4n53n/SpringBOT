package Utility;

import java.util.Random;

public class RSJG {

    public static String get(){
        String RandomSkeletonJokeGenerator = null;

        Random random = new Random();
        int rng = random.nextInt(11);

        switch (rng){
            case 1:
                RandomSkeletonJokeGenerator = "Who is the most famous skeleton detective? Sherlock Bones.";
                break;
            case 2:
                RandomSkeletonJokeGenerator = "What was the skeleton's favorite musical instrument? The trom-bone.";
                break;
            case 3:
                RandomSkeletonJokeGenerator = "What do you call a skeleton who won't work? Lazy bones.";
                break;
            case 4:
                RandomSkeletonJokeGenerator = "What song do skeleton bikers ride to? Bone to be wild.";
                break;
            case 5:
                RandomSkeletonJokeGenerator = "What do skeletons say as they head out to sea? Bone voyage!";
                break;
            case 6:
                RandomSkeletonJokeGenerator = "Why did the skeleton have to goto church to play music? They don't have any organs!";
                break;
            case 7:
                RandomSkeletonJokeGenerator = "How did the skeleton know it was going to rain? It could feel it in it's bones.";
                break;
            case 8:
                RandomSkeletonJokeGenerator = "What do skeletons order at restaurants? Spare ribs!";
                break;
            case 9:
                RandomSkeletonJokeGenerator = "Why are skeletons so calm? Because nothing gets under their skin.";
                break;
            case 10:
                RandomSkeletonJokeGenerator = "Why didn't the skeleton go to the scary movie? He didn't have the guts!";
                break;
        }

        return RandomSkeletonJokeGenerator;
    }

}
