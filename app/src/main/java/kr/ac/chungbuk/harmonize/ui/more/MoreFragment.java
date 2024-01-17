package kr.ac.chungbuk.harmonize.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.utility.adapter.MoreMenuAdapter;

public class MoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_more, container, false);


        ListView menuListView = root.findViewById(R.id.menuListView);
        MoreMenuAdapter adapter = new MoreMenuAdapter(getContext());
        menuListView.setAdapter(adapter);


        return root;
    }

}
