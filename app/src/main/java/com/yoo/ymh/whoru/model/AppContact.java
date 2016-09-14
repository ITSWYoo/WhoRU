package com.yoo.ymh.whoru.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-08-14.
 */
public class AppContact implements Parcelable {

    @SerializedName("_id")
    @Expose
    private int id;
    @SerializedName("_provider")
    @Expose
    private String provider;
    @SerializedName("_providerId")
    @Expose
    private int providerId;
    @SerializedName("_fcmToken")
    @Expose
    private String fcmToken;
    @SerializedName("_name")
    @Expose
    private String name;
    @SerializedName("_phone")
    @Expose
    private String phone;
    @SerializedName("_company")
    @Expose
    private String company;
    @SerializedName("_department")
    @Expose
    private String department;
    @SerializedName("_responsibility")
    @Expose
    private String responsibility;
    @SerializedName("_memo")
    @Expose
    private String memo;
    @SerializedName("_companyPhone")
    @Expose
    private String companyPhone;
    @SerializedName("_faxPhone")
    @Expose
    private String faxPhone;
    @SerializedName("_email")
    @Expose
    private String email;
    @SerializedName("_companyAddress")
    @Expose
    private String companyAddress;
    @SerializedName("_facebookAddress")
    @Expose
    private String facebookAddress;
    @SerializedName("_googleAddress")
    @Expose
    private String googleAddress;
    @SerializedName("_instagramAddress")
    @Expose
    private String instagramAddress;
    @SerializedName("_linkedinAddress")
    @Expose
    private String linkedinAddress;
    @SerializedName("_blogAddress")
    @Expose
    private String blogAddress;
    @SerializedName("_extraAddress")
    @Expose
    private String extraAddress;
    @SerializedName("_group")
    @Expose
    private List<Group> group = new ArrayList<>();
    @SerializedName("_profileImage")
    @Expose
    private String profileImage;
    @SerializedName("_profileThumbnail")
    @Expose
    private String profileThumbnail;
    @SerializedName("_cardImage")
    @Expose
    private String cardImage;
    @SerializedName("_cardImage1")
    @Expose
    private String cardImage_back;
    @SerializedName("_cardThumbnail")
    @Expose
    private String cardImageThumbnail;
    @SerializedName("_cardThumbnail1")
    @Expose
    private String cardImage_backThumbnail;
    private boolean selected;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The _id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider The _provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return The providerId
     */
    public int getProviderId() {
        return providerId;
    }

    /**
     * @param providerId The _providerId
     */
    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    /**
     * @return The fcmToken
     */
    public String getFcmToken() {
        return fcmToken;
    }

    /**
     * @param fcmToken The _fcmToken
     */
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The _name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The _phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return The company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company The _company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department The _department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return The responsibility
     */
    public String getResponsibility() {
        return responsibility;
    }

    /**
     * @param responsibility The _responsibility
     */
    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return The companyPhone
     */
    public String getCompanyPhone() {
        return companyPhone;
    }

    /**
     * @param companyPhone The _companyPhone
     */
    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    /**
     * @return The faxPhone
     */
    public String getFaxPhone() {
        return faxPhone;
    }

    /**
     * @param faxPhone The _faxPhone
     */
    public void setFaxPhone(String faxPhone) {
        this.faxPhone = faxPhone;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The _email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The companyAddress
     */
    public String getCompanyAddress() {
        return companyAddress;
    }

    /**
     * @param companyAddress The _companyAddress
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    /**
     * @return The facebookAddress
     */
    public String getFacebookAddress() {
        return facebookAddress;
    }

    /**
     * @param facebookAddress The _facebookAddress
     */
    public void setFacebookAddress(String facebookAddress) {
        this.facebookAddress = facebookAddress;
    }

    /**
     * @return The googleAddress
     */
    public String getGoogleAddress() {
        return googleAddress;
    }

    /**
     * @param googleAddress The _googleAddress
     */
    public void setGoogleAddress(String googleAddress) {
        this.googleAddress = googleAddress;
    }

    /**
     * @return The instagramAddress
     */
    public String getInstagramAddress() {
        return instagramAddress;
    }

    /**
     * @param instagramAddress The _instagramAddress
     */
    public void setInstagramAddress(String instagramAddress) {
        this.instagramAddress = instagramAddress;
    }

    /**
     * @return The linkedinAddress
     */
    public String getLinkedinAddress() {
        return linkedinAddress;
    }

    /**
     * @param linkedinAddress The _linkedinAddress
     */
    public void setLinkedinAddress(String linkedinAddress) {
        this.linkedinAddress = linkedinAddress;
    }

    /**
     * @return The blogAddress
     */
    public String getBlogAddress() {
        return blogAddress;
    }

    /**
     * @param blogAddress The _blogAddress
     */
    public void setBlogAddress(String blogAddress) {
        this.blogAddress = blogAddress;
    }

    /**
     * @return The extraAddress
     */
    public String getExtraAddress() {
        return extraAddress;
    }

    /**
     * @param extraAddress The _extraAddress
     */
    public void setExtraAddress(String extraAddress) {
        this.extraAddress = extraAddress;
    }

    /**
     * @return The group
     */
    public List<Group> getGroup() {
        return group;
    }

    /**
     * @param group The _group
     */
    public void setGroup(List<Group> group) {
        this.group = group;
    }

    /**
     * @return The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * @param profileImage The _profileImage
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * @return The profileThumbnail
     */
    public String getProfileThumbnail() {
        return profileThumbnail;
    }

    /**
     * @param profileThumbnail The _profileThumbnail
     */
    public void setProfileThumbnail(String profileThumbnail) {
        this.profileThumbnail = profileThumbnail;
    }

    /**
     * @return The cardImage
     */

    public String getCardImage() {
        return cardImage;
    }

    /**
     * @param cardImage The _cardImage
     */
    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    /**
     * @return The cardImage_barck
     */

    public String getCardImage_back() {
        return cardImage_back;
    }

    /**
     * @param cardImage_back The _cardImage_back
     */
    public void setCardImage_back(String cardImage_back) {
        this.cardImage_back = cardImage_back;
    }

    /**
     * @return The cardImageThumbnail
     */
    public String getCardImageThumbnail() {
        return cardImageThumbnail;
    }

    /**
     * @param cardImageThumbnail The _cardImageThumbnail
     */
    public void setCardImageThumbnail(String cardImageThumbnail) {
        this.cardImageThumbnail = cardImageThumbnail;
    }

    /**
     * @return The cardImage_backThumbnail
     */
    public String getCardImage_backThumbnail() {
        return cardImage_backThumbnail;
    }

    /**
     * @param cardImage_backThumbnail The _cardImage_backThumbnail
     */
    public void setCardImage_backThumbnail(String cardImage_backThumbnail) {
        this.cardImage_backThumbnail = cardImage_backThumbnail;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "AppContact{" +
                "id=" + id +
                ", provider='" + provider + '\'' +
                ", providerId=" + providerId +
                ", fcmToken=" + fcmToken +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", company='" + company + '\'' +
                ", department='" + department + '\'' +
                ", responsibility='" + responsibility + '\'' +
                ", memo='" + memo + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", faxPhone='" + faxPhone + '\'' +
                ", email='" + email + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", facebookAddress='" + facebookAddress + '\'' +
                ", googleAddress='" + googleAddress + '\'' +
                ", instagramAddress='" + instagramAddress + '\'' +
                ", linkedinAddress='" + linkedinAddress + '\'' +
                ", blogAddress='" + blogAddress + '\'' +
                ", extraAddress='" + extraAddress + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", profileThumbnail" + profileThumbnail + '\'' +
                ", cardImage='" + cardImage + '\'' +
                ", cardImage_back='" + cardImage_back + '\'' +
                ", cardImageThumbnail" + cardImageThumbnail + '\'' +
                ", selected=" + selected +
                '}';
    }

    public AppContact() {
        this.setId(0);
        this.setName("");
        this.setPhone("");
        this.setMemo("");
        this.setEmail("");
        this.setCardImage("");
        this.setCardImageThumbnail("");
        this.setCardImage_back("");
        this.setCardImage_backThumbnail("");
        this.setCompany("");
        this.setCompanyAddress("");
        this.setCompanyPhone("");
        this.setFacebookAddress("");
        this.setInstagramAddress("");
        this.setLinkedinAddress("");
        this.setExtraAddress("");
        this.setDepartment("");
        this.setResponsibility("");
        this.setGroup(null);
        this.setGoogleAddress("");
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.provider);
        dest.writeInt(this.providerId);
        dest.writeString(this.fcmToken);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.company);
        dest.writeString(this.department);
        dest.writeString(this.responsibility);
        dest.writeString(this.memo);
        dest.writeString(this.companyPhone);
        dest.writeString(this.faxPhone);
        dest.writeString(this.email);
        dest.writeString(this.companyAddress);
        dest.writeString(this.facebookAddress);
        dest.writeString(this.googleAddress);
        dest.writeString(this.instagramAddress);
        dest.writeString(this.linkedinAddress);
        dest.writeString(this.blogAddress);
        dest.writeString(this.extraAddress);
        dest.writeList(this.group);
        dest.writeString(this.profileImage);
        dest.writeString(this.profileThumbnail);
        dest.writeString(this.cardImage);
        dest.writeString(this.cardImage_back);
        dest.writeString(this.cardImageThumbnail);
        dest.writeString(this.cardImage_backThumbnail);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected AppContact(Parcel in) {
        this.id = in.readInt();
        this.provider = in.readString();
        this.providerId = in.readInt();
        this.fcmToken = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.company = in.readString();
        this.department = in.readString();
        this.responsibility = in.readString();
        this.memo = in.readString();
        this.companyPhone = in.readString();
        this.faxPhone = in.readString();
        this.email = in.readString();
        this.companyAddress = in.readString();
        this.facebookAddress = in.readString();
        this.googleAddress = in.readString();
        this.instagramAddress = in.readString();
        this.linkedinAddress = in.readString();
        this.blogAddress = in.readString();
        this.extraAddress = in.readString();
        this.group = new ArrayList<>();
        in.readList(this.group, Group.class.getClassLoader());
        this.profileImage = in.readString();
        this.profileThumbnail = in.readString();
        this.cardImage = in.readString();
        this.cardImage_back = in.readString();
        this.cardImageThumbnail = in.readString();
        this.cardImage_backThumbnail = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<AppContact> CREATOR = new Creator<AppContact>() {
        @Override
        public AppContact createFromParcel(Parcel source) {
            return new AppContact(source);
        }

        @Override
        public AppContact[] newArray(int size) {
            return new AppContact[size];
        }
    };
}
