package edu.mssm.pharm.maayanlab.Harmonizome.util;

import java.util.Comparator;

import edu.mssm.pharm.maayanlab.Harmonizome.model.Download;

public class DownloadComparator implements Comparator<Download> {

    @Override
    public int compare(Download d1, Download d2) {
        return d1.getDownloadType().getOrdering() > d2.getDownloadType().getOrdering() ? 1 : 0;
    }
}