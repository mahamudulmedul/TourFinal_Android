package bd.org.bitm.mad.batch33.tourmate.fragment.Expense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bd.org.bitm.mad.batch33.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseListFragment extends Fragment {


    public ExpenseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }

}
