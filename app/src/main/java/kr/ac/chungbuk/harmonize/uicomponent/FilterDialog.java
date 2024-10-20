package kr.ac.chungbuk.harmonize.uicomponent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    AppCompatButton btnReset, btnApply;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_genre, R.layout.genre_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        genderToggleGroup = view.findViewById(R.id.genderToggleGroup);
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);

        // 받아온 이전 필터 설정 값으로 초기화
        FilterValue prevFilter = new FilterValue();
        prevFilter.setFromString(getArguments().getString("prevFilter"));
        filterValue = prevFilter;

        if (prevFilter.getTypeSolo())
            genderToggleGroup.check(R.id.typeSolo);
        if (prevFilter.getTypeGroup())
            genderToggleGroup.check(R.id.typeGroup);

        spinner.setSelection(getSpinnerSelectionIndex(prevFilter.getGenre()));
        
        
        // EventListener 추가
        genderToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.typeSolo)
                    filterValue.setTypeSolo(isChecked);
                else if (checkedId == R.id.typeGroup)
                    filterValue.setTypeGroup(isChecked);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterValue.setGenre(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterValue.setDefault();
                mCallback.onApplyFilter(filterValue);
                dismiss();
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

    private int getSpinnerSelectionIndex(String genre) {
        return switch (genre) {
            case "가요" -> 1;
            case "팝송" -> 2;
            case "발라드" -> 3;
            case "랩/힙합" -> 4;
            case "댄스" -> 5;
            case "일본곡" -> 6;
            case "R&B" -> 7;
            case "OST" -> 8;
            case "인디뮤직" -> 9;
            case "트로트" -> 10;
            case "어린이곡" -> 11;
            default -> 0;
        };
    }
}
