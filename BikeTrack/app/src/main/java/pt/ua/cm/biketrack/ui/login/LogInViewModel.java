package pt.ua.cm.biketrack.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogInViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public LogInViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is login fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
