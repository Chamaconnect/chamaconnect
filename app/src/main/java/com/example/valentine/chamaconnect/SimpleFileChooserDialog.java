package com.example.valentine.chamaconnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleFileChooserDialog {
    public final static int FILE_SELECT = 0;
    public final static int FILE_SAVE = 1;
    public final static int FOLDER_SELECT = 2;
    private ImageButton backButton;
    private int mSelectionType;
    private String mDirSdCardPath = "";
    private Context mContext;
    private TextView tv_mDialogTitle;
    private TextView tv_mDialogPath;
    private String mSelectedFileName;

    public String mPosButtonText;
    public String mNegButtonText;
    public String mNewFileDialogTitle;
    public String mDialogTitle;
    public int mRevertDirDrawable;
    public int mNewFolderDrawable;
    public String mNewFolderPosButtonText;
    public String mNewFolderNegButtonText;
    public String mNewFolderFailText;
    public String mNewFolderDialogTitle;
    public Boolean mHideBackButtonInRootDir;
    public String mDialogTitleBackgroundColorHex;
    public String mDialogDividerColorHex;
    public String mFileSaveNoNameFailureText;
    public ArrayList<String> mAllowedFileExtsList;

    private String mDir = "";
    private List<String> mSubDirs = null;
    private SimpleFileDialogListener mSimpleFileDialogListener = null;
    private ArrayAdapter<String> mListAdapter;
    private boolean mEnteredFilename;


    // Callback interface for selected directory
    public interface SimpleFileDialogListener {
        void onPositiveButton(String path);
    }

    // Constructor for the class
    public SimpleFileChooserDialog(Context context, int file_select_type, SimpleFileDialogListener SimpleFileDialogListener) {
        if (file_select_type == FILE_SELECT)
            mSelectionType = FILE_SELECT;
        else if (file_select_type == FILE_SAVE)
            mSelectionType = FILE_SAVE;
        else if (file_select_type == FOLDER_SELECT)
            mSelectionType = FOLDER_SELECT;
        else mSelectionType = FILE_SELECT;
        mContext = context;
        mDirSdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mSimpleFileDialogListener = SimpleFileDialogListener;
        initialiseOptionalFields();
    }

    private void initialiseOptionalFields() {
        mPosButtonText = "Ok";
        mNegButtonText = "Cancel";
        if (mSelectionType == FILE_SELECT)      mDialogTitle = "Select file";
        if (mSelectionType == FILE_SAVE)        mDialogTitle = "Save file";
        if (mSelectionType == FOLDER_SELECT)    mDialogTitle = "Select folder";
        mRevertDirDrawable = android.R.drawable.ic_menu_revert;
        mNewFolderDrawable = android.R.drawable.ic_menu_add;
        mNewFolderFailText = "Failed to create";
        mNewFolderPosButtonText = "Ok";
        mNewFolderNegButtonText = "Cancel";
        mNewFileDialogTitle = "Enter file name";
        mNewFolderDialogTitle = "Enter folder name";
        mHideBackButtonInRootDir = true;
        mFileSaveNoNameFailureText = "You need to enter a valid name for the file to save.";
        mDialogTitleBackgroundColorHex = "#FF373737";
        mDialogDividerColorHex = mDialogTitleBackgroundColorHex;
        mAllowedFileExtsList = new ArrayList<>();
        mEnteredFilename = false;
    }

    // If no directory is chosen, the standard SD-card directory will be used
    public void chooseFile_or_Dir() {
        if (mDir.equals(""))
            chooseFile_or_Dir(mDirSdCardPath);
        else
            chooseFile_or_Dir(mDir);
    }

    // If directory is chosen, use it, instead of standard dir.
    public void chooseFile_or_Dir(String dir) {
        if (mSelectionType == FILE_SAVE) {
            if (!mEnteredFilename) {
                simpleDialogEnterText();
                return;
            }
        }

        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory())    // if the given dir is invalid, use standard
            dir = mDirSdCardPath;

        try {
            dir = new File(dir).getCanonicalPath();
        } catch (IOException ioe) {
            System.err.println("Couldn't get the canonical path of >" + mDirSdCardPath + "<.");
            return;
        }
        mDir = dir;
        mSubDirs = getDirectories(mDir);

        class SimpleFileDialogOnClickListener implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int item) {
                String mDirOld = mDir;
                String sel = "" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
                if (sel.charAt(sel.length()-1) == '/')
                    sel = sel.substring(0, sel.length()-1);
                // Navigate into the sub-directory
                if (new File(mDir + File.separator +sel).isFile()) {    // If the selection is a regular file
                    mDir = mDirOld;
                    mSelectedFileName = sel;
                } else {
                    mDir += File.separator + sel;
                }
                if (mSelectionType == FILE_SELECT && mSelectedFileName != null ) {
                    // Do this, to instantly return the full path to the file, without clicking pos. Button
                    mSimpleFileDialogListener.onPositiveButton(mDir + File.separator + mSelectedFileName);
                    dialog.dismiss();
                    return;
                }
                updateDirectory();
            }
        }
        AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(dir, mSubDirs, new SimpleFileDialogOnClickListener());
        // Positive Button not enabled for file select (look 9 rows higher - 177)
        if (mSelectionType != FILE_SELECT) {
            dialogBuilder.setPositiveButton(mPosButtonText, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mSimpleFileDialogListener != null) {
                        if (mSelectionType == FILE_SAVE) {
                            mSimpleFileDialogListener.onPositiveButton(mDir + File.separator + mSelectedFileName);
                        } else {
                            mSimpleFileDialogListener.onPositiveButton(mDir);
                        }
                    }
                }
            });
        }
        dialogBuilder.setNegativeButton(mNegButtonText, null);
        final AlertDialog dirsDialog = dialogBuilder.create();
        int dividerID = dirsDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        dirsDialog.show();
        if (dividerID != 0) {
            View mDivider = dirsDialog.findViewById(dividerID);
            if (mDivider != null) {
                mDivider.setBackgroundColor(Color.parseColor(mDialogDividerColorHex));
            }
        }
    }

    private boolean createSubDir(String newDir) {
        File newDirFile = new File(newDir);
        return !newDirFile.exists()? newDirFile.mkdir() : false;
    }

    private List<String> getDirectories(String tmpDir) {
        File dir = new File(tmpDir);
        List<String> dirs = new ArrayList<>();
        System.out.println("DIR: " + dir.toString());
        if (!dir.exists() || !dir.isDirectory())
            return dirs;
        if (dir.listFiles() == null) {
            System.err.println("ListFiles: No files to pick!");
            return dirs;
        }
        for (File file : dir.listFiles()) {
            if ( file.isDirectory()) {
                dirs.add( file.getName() + "/" );       // Add "/" to directory names to identify them in the list
            } else if (mSelectionType == FILE_SAVE || mSelectionType == FILE_SELECT) {
                String fName = file.getName();
                if (mAllowedFileExtsList.size() > 0) {
                    if (fName.contains(".")) {
                        if (mAllowedFileExtsList.contains(fName.substring(fName.lastIndexOf("."), fName.length()))) {
                            dirs.add(file.getName());             // Add file names to the list if we are doing a file save or file open
                        }
                    }
                } else {
                    dirs.add(file.getName());
                }
            }
        }
        Collections.sort(dirs, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return dirs;
    }

    // DIALOG BUILDER
    private AlertDialog.Builder createDirectoryChooserDialog(
            final String title, List<String> listItems, DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        container.setBackgroundColor(Color.parseColor(mDialogTitleBackgroundColorHex));

        LinearLayout titleCont = new LinearLayout(mContext);
        titleCont.setOrientation(LinearLayout.HORIZONTAL);
        titleCont.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        titleCont.setPadding(25, 15, 0, 0);
        titleCont.setGravity(Gravity.CENTER_VERTICAL);
        titleCont.setBackgroundColor(Color.parseColor(mDialogTitleBackgroundColorHex));

        LinearLayout buttonCont = new LinearLayout(mContext);
        buttonCont.setOrientation(LinearLayout.HORIZONTAL);
        buttonCont.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        buttonCont.setPadding(15,0,15,0);
        buttonCont.setBackgroundColor(Color.parseColor(mDialogTitleBackgroundColorHex));

        tv_mDialogTitle = new TextView(mContext);
        tv_mDialogTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4.0f));
        tv_mDialogTitle.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
        //tv_mDialogTitle.setTextColor( mContext.getResources().getColor(android.R.color.black) );
        tv_mDialogTitle.setText(mDialogTitle);
        tv_mDialogTitle.setTextColor(mContext.getResources().getColor(android.R.color.white));

        // Add back button
        backButton = new ImageButton(mContext);
        backButton.setImageDrawable(mContext.getResources().getDrawable(mRevertDirDrawable));
        if (mHideBackButtonInRootDir) backButton.setVisibility(View.GONE);
        backButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDir.equals(mDirSdCardPath))
                    mDir = mDir.substring(0, mDir.lastIndexOf("/"));
                updateDirectory();
                if (mHideBackButtonInRootDir) {
                    if (mDir.equals(mDirSdCardPath))
                        backButton.setVisibility(View.GONE);
                }
            }
        });

        // Add Title and Buttons to Top of Dialog
        if (mSelectionType == FILE_SELECT) {
            titleCont.addView(backButton);
            titleCont.addView(tv_mDialogTitle);
            container.addView(titleCont);
        } else {
            // Add new folder button if necessary
            titleCont.addView(tv_mDialogTitle);
            buttonCont.addView(backButton);
            ImageButton newDirButton = new ImageButton(mContext);
            newDirButton.setImageDrawable(mContext.getResources().getDrawable(mNewFolderDrawable));
            newDirButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
            newDirButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {                       // Show new folder name input dialog
                    final EditText input = new EditText(mContext);
                    input.setGravity(Gravity.CENTER);
                    LinearLayout titleCont = new LinearLayout(mContext);
                    titleCont.setOrientation(LinearLayout.HORIZONTAL);
                    titleCont.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    titleCont.setPadding(25, 15, 0, 0);
                    titleCont.setGravity(Gravity.CENTER_VERTICAL);
                    titleCont.setBackgroundColor(Color.parseColor(mDialogTitleBackgroundColorHex));
                    TextView tv_mNewFolderDialogTitle = new TextView(mContext);
                    tv_mNewFolderDialogTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4.0f));
                    tv_mNewFolderDialogTitle.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
                    tv_mNewFolderDialogTitle.setText(mNewFolderDialogTitle);
                    tv_mNewFolderDialogTitle.setTextColor(mContext.getResources().getColor(android.R.color.white));
                    titleCont.addView(tv_mNewFolderDialogTitle);
                    AlertDialog newFolder = new AlertDialog.Builder(mContext).setCustomTitle(titleCont).setView(input)
                            .setPositiveButton(mNewFolderPosButtonText, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Editable newDir = input.getText();
                                    String newDirName = newDir.toString();
                                    if (createSubDir(mDir + "/" + newDirName)) {       // Create new directory
                                        mDir += "/" + newDirName;                        // Navigate into the new directory
                                        updateDirectory();
                                    } else {
                                        Toast.makeText(mContext, mNewFolderFailText+" '" + newDirName + "'", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton(mNewFolderNegButtonText, null).show();
                    int dividerID = newFolder.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                    View mDivider = newFolder.findViewById(dividerID);
                    mDivider.setBackgroundColor(Color.parseColor(mDialogDividerColorHex));
                }
            });
            buttonCont.addView(newDirButton);
            container.addView(titleCont);
            container.addView(buttonCont);
        }
        // Create View with folder path and entry text box
        LinearLayout pathCont = new LinearLayout(mContext);
        pathCont.setOrientation(LinearLayout.HORIZONTAL);
        pathCont.setBackgroundColor(Color.parseColor(mDialogTitleBackgroundColorHex));

        tv_mDialogPath = new TextView(mContext);
        tv_mDialogPath.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tv_mDialogPath.setTextColor(mContext.getResources().getColor(android.R.color.white));
        tv_mDialogPath.setGravity(Gravity.CENTER_VERTICAL);
        tv_mDialogPath.setText(title);

        pathCont.addView(tv_mDialogPath);
        pathCont.setPadding(15,0,0,0);
        container.addView(pathCont);

        // Set Views and Finish Dialog builder  //
        dialogBuilder.setCustomTitle(container);
        mListAdapter = createListAdapter(listItems);
        dialogBuilder.setSingleChoiceItems(mListAdapter, -1, onClickListener);
        dialogBuilder.setCancelable(false);
        return dialogBuilder;
    }

    private void updateDirectory() {
        mSubDirs.clear();
        mSubDirs.addAll(getDirectories(mDir));
        tv_mDialogPath.setText(mDir);
        mListAdapter.notifyDataSetChanged();
        if (mHideBackButtonInRootDir) {
            if (!mDir.equals(mDirSdCardPath)) {
                backButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private ArrayAdapter<String> createListAdapter(List<String> items) {
        return new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_item, android.R.id.text1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView) {
                    // Enable list item (directory) text wrapping
                    TextView tv = (TextView) v;
                    float myDensity = mContext.getResources().getDisplayMetrics().density;
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (55/myDensity));
                    tv.getLayoutParams().height =  (int)(tv.getTextSize()*2.5);
                    tv.setEllipsize(null);
                }
                return v;
            }
        };
    }

    private Button posButton = null;   // New filename Dialog positive button
    private void simpleDialogEnterText() {
        final EditText input = new EditText(mContext);
        input.addTextChangedListener(new fileExtensionCheckListener(input));
        input.setGravity(Gravity.CENTER);
        LinearLayout titleCont = new LinearLayout(mContext);
        titleCont.setOrientation(LinearLayout.HORIZONTAL);
        titleCont.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        titleCont.setPadding(25, 15, 0, 0);
        titleCont.setGravity(Gravity.CENTER_VERTICAL);
        titleCont.setBackgroundColor(Color.parseColor(mDialogTitleBackgroundColorHex));
        TextView tv_mNewFolderDialogTitle = new TextView(mContext);
        tv_mNewFolderDialogTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4.0f));
        tv_mNewFolderDialogTitle.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
        tv_mNewFolderDialogTitle.setText(mNewFileDialogTitle);
        tv_mNewFolderDialogTitle.setTextColor(mContext.getResources().getColor(android.R.color.white));
        titleCont.addView(tv_mNewFolderDialogTitle);
        AlertDialog.Builder newFolder = new AlertDialog.Builder(mContext).setCustomTitle(titleCont).setView(input)
                .setPositiveButton(mNewFolderPosButtonText, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newDir = String.valueOf(input.getText());
                        if (newDir == null || newDir.equals("")) {
                            Toast.makeText(mContext, mFileSaveNoNameFailureText, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mEnteredFilename = true;
                        mSelectedFileName = newDir;
                        mDialogTitle = mDialogTitle + " '" + mSelectedFileName + "'";
                        chooseFile_or_Dir();
                    }
                }).setNegativeButton(mNewFolderNegButtonText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, mFileSaveNoNameFailureText, Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog newFolderDialog = newFolder.create();
        newFolderDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            }
        });
        newFolderDialog.show();
        int dividerID = newFolderDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        if (dividerID != 0) {
            View mDivider = newFolderDialog.findViewById(dividerID);
            if (mDivider != null) {
                mDivider.setBackgroundColor(Color.parseColor(mDialogDividerColorHex));
            }
        }
    }

    private class fileExtensionCheckListener implements TextWatcher {
        EditText inputField;
        public fileExtensionCheckListener(EditText input) {
            inputField = input;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = String.valueOf(inputField.getText());
            if (text.length() == 0 || text == null || text.equals("")) {
                if (posButton != null) posButton.setEnabled(false);
                Toast.makeText(mContext, mFileSaveNoNameFailureText, Toast.LENGTH_SHORT).show();
            } else if (text.contains(".") && mAllowedFileExtsList.size() > 0) {
                if (!mAllowedFileExtsList.contains(text.substring(text.lastIndexOf(".")))
                        && text.substring(text.lastIndexOf(".")).length() > 3) {
                    if (posButton != null) posButton.setEnabled(false);
                    Toast.makeText(mContext, mFileSaveNoNameFailureText, Toast.LENGTH_SHORT).show();
                } else {
                    if (posButton != null) posButton.setEnabled(true);
                }
            } else {
                if (posButton != null) posButton.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
    }
}