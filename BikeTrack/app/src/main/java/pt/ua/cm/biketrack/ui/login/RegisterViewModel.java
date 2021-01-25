package pt.ua.cm.biketrack.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public RegisterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is register fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
