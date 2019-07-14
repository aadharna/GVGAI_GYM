package kkunan.FeatureState;


public interface LearningState {


    static double[] generateFeatureFromState(AvatarInfoState learningstate){
        return learningstate.generatedFeatureFromState();
    }

    double[] generatedFeatureFromState();

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object state);

}
