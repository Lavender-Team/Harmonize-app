package kr.ac.chungbuk.harmonize.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.ui.test.TestActivity;
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

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 서비스 소개 클릭시
                        startActivity(new Intent(getActivity(), TestActivity.class)); break;
                    default:
                        break;
                }
            }
        });

        return root;
    }

}
