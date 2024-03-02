package kr.ac.chungbuk.harmonize.uicomponent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButtonToggleGroup;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.ui.search.FilterValue;
import kr.ac.chungbuk.harmonize.ui.search.IFilterApply;

public class FilterDialog extends DialogFragment {

    private FilterValue filterValue;
    private IFilterApply mCallback;

    MaterialButtonToggleGroup genderToggleGroup;
    AppCompatButton btnApply;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_genre, R.layout.genre_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        genderToggleGroup = view.findViewById(R.id.genderToggleGroup);
        btnApply = view.findViewById(R.id.btnApply);

        // 받아온 이전 필터 설정 값으로 초기화
        FilterValue prevFilter = new FilterValue();
        prevFilter.setFromString(getArguments().getString("prevFilter"));
        filterValue = prevFilter;

        if (prevFilter.isGenderMale())
            genderToggleGroup.check(R.id.genderMale);
        if (prevFilter.isGenderFemale())
            genderToggleGroup.check(R.id.genderFemale);
        if (prevFilter.isGenderMixed())
            genderToggleGroup.check(R.id.genderMixed);

        
        
        // EventListener 추가
        genderToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.genderMale)
                    filterValue.setGenderMale(isChecked);
                else if (checkedId == R.id.genderFemale)
                    filterValue.setGenderFemale(isChecked);
                else if (checkedId == R.id.genderMixed)
                    filterValue.setGenderMixed(isChecked);
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onApplyFilter(filterValue);
                dismiss();
            }
        });
        

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mCallback = (IFilterApply) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement IFilterApply interface");
        }
    }

}
