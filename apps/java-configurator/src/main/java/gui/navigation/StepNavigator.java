package gui.navigation;

public interface StepNavigator {
    void goToStep(int idx);

    // Adăugăm o metodă pentru a notifica panourile când devin active
    default void onStepActivated(int step) {}
}