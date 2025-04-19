package com.example.soundslike.ui.artistdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.soundslike.data.models.Artist;
import com.example.soundslike.data.repository.ArtistRepository;

public class ArtistDetailViewModel extends ViewModel {

    private static final String ARTIST_ID_KEY = "artistId"; // Key for SavedStateHandle

    private final ArtistRepository artistRepository;
    private final SavedStateHandle savedStateHandle;

    private final MediatorLiveData<Artist> _artistDetails = new MediatorLiveData<>();
    public LiveData<Artist> getArtistDetails() { return _artistDetails; }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading() { return _isLoading; }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() { return _errorMessage; }

    private LiveData<Artist> artistSource = null;

    public ArtistDetailViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.artistRepository = new ArtistRepository();
        loadArtistData();
    }

    private void loadArtistData() {
        String artistId = savedStateHandle.get(ARTIST_ID_KEY);

        if (artistId == null || artistId.isEmpty()) {
            _errorMessage.setValue("Invalid Artist ID.");
            _isLoading.setValue(false);
            return;
        }

        if (artistSource != null) {
            _artistDetails.removeSource(artistSource);
        }

        _isLoading.setValue(true);
        _errorMessage.setValue(null);

        artistSource = artistRepository.getArtistDetails(artistId);

        _artistDetails.addSource(artistSource, artist -> {
            _isLoading.setValue(false);
            if (artist != null) {
                _artistDetails.setValue(artist);
            } else {
                _artistDetails.setValue(null);
                _errorMessage.setValue("Failed to load artist details.");
            }
        });
    }

    @Override
    protected void onCleared() {
        if (artistSource != null) {
            _artistDetails.removeSource(artistSource);
        }
        super.onCleared();
    }
}