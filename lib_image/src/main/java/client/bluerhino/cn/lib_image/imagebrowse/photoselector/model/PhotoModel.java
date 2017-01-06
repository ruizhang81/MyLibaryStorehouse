package client.bluerhino.cn.lib_image.imagebrowse.photoselector.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aizaz
 */


public class PhotoModel implements Parcelable {

    public static final Creator<PhotoModel> CREATOR = new Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel in) {
            return new PhotoModel(in);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };
    private static final long serialVersionUID = 1L;
    private String originalPath;
    private boolean isChecked;

    public PhotoModel(String originalPath, boolean isChecked) {
        super();
        this.originalPath = originalPath;
        this.isChecked = isChecked;
    }

    public PhotoModel(String originalPath) {
        this.originalPath = originalPath;
    }

    public PhotoModel() {
    }

    protected PhotoModel(Parcel in) {
        originalPath = in.readString();
        isChecked = in.readByte() != 0;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public boolean isChecked() {
        return isChecked;
    }

//	@Override
//	public boolean equals(Object o) {
//		if (o.getClass() == getClass()) {
//			PhotoModel model = (PhotoModel) o;
//			if (this.getOriginalPath().equals(model.getOriginalPath())) {
//				return true;
//			}
//		}
//		return false;
//	}

    public void setChecked(boolean isChecked) {
        //System.out.println("checked " + isChecked + " for " + originalPath);
        this.isChecked = isChecked;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((originalPath == null) ? 0 : originalPath.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PhotoModel)) {
            return false;
        }
        PhotoModel other = (PhotoModel) obj;
        if (originalPath == null) {
            if (other.originalPath != null) {
                return false;
            }
        } else if (!originalPath.equals(other.originalPath)) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalPath);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
