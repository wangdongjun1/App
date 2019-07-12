package com.jarisoft.entity;

import java.io.Serializable;

/**
 * 管件信息
 * Created by shanwj on 2018/7/20.
 */

public class PileInfo implements Serializable{
    private String HandoverNum;
    private String ProjectNum;
    private String ChartNum;
    private String PipeNum;
    private String BarCode;
    private String QRCode;
    private String PDFUrl;
    private String InstallTrayNum;
    private String InsideCode;
    private String Subsection;
    private String PipeMaterial;
    private String PipeSpec;
    private String PipeLength;
    private String PipeWeight;
    private String SurfaceCode;
    private String PipeType;

    public PileInfo() {
    }

    public PileInfo(String projectNum, String chartNum, String pipeNum, String barCode) {
        ProjectNum = projectNum;
        ChartNum = chartNum;
        PipeNum = pipeNum;
        BarCode = barCode;
    }

    /**
     * 出厂使用
     * @param handoverNum
     * @param projectNum
     * @param chartNum
     * @param pipeNum
     * @param barCode
     * @param QRCode
     * @param PDFUrl
     */
    public PileInfo(String handoverNum, String projectNum, String chartNum, String pipeNum, String barCode, String QRCode, String PDFUrl) {
        HandoverNum = handoverNum;
        ProjectNum = projectNum;
        ChartNum = chartNum;
        PipeNum = pipeNum;
        BarCode = barCode;
        this.QRCode = QRCode;
        this.PDFUrl = PDFUrl;
    }

    /**
     * 回厂使用
     * @param handoverNum
     * @param projectNum
     * @param chartNum
     * @param pipeNum
     * @param QRCode
     * @param PDFUrl
     */
    public PileInfo(String handoverNum, String projectNum, String chartNum, String pipeNum, String QRCode, String PDFUrl) {
        HandoverNum = handoverNum;
        ProjectNum = projectNum;
        ChartNum = chartNum;
        PipeNum = pipeNum;
        this.QRCode = QRCode;
        this.PDFUrl = PDFUrl;
    }

    public String getHandoverNum() {
        return HandoverNum;
    }

    public void setHandoverNum(String handoverNum) {
        HandoverNum = handoverNum;
    }

    public String getProjectNum() {
        return ProjectNum;
    }

    public void setProjectNum(String projectNum) {
        ProjectNum = projectNum;
    }

    public String getChartNum() {
        return ChartNum;
    }

    public void setChartNum(String chartNum) {
        ChartNum = chartNum;
    }

    public String getPipeNum() {
        return PipeNum;
    }

    public void setPipeNum(String pipeNum) {
        PipeNum = pipeNum;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getPDFUrl() {
        return PDFUrl;
    }

    public void setPDFUrl(String PDFUrl) {
        this.PDFUrl = PDFUrl;
    }

    public String getInstallTrayNum() {
        return InstallTrayNum;
    }

    public void setInstallTrayNum(String installTrayNum) {
        InstallTrayNum = installTrayNum;
    }

    public String getInsideCode() {
        return InsideCode;
    }

    public void setInsideCode(String insideCode) {
        InsideCode = insideCode;
    }

    public String getSubsection() {
        return Subsection;
    }

    public void setSubsection(String subsection) {
        Subsection = subsection;
    }

    public String getPipeMaterial() {
        return PipeMaterial;
    }

    public void setPipeMaterial(String pipeMaterial) {
        PipeMaterial = pipeMaterial;
    }

    public String getPipeSpec() {
        return PipeSpec;
    }

    public void setPipeSpec(String pipeSpec) {
        PipeSpec = pipeSpec;
    }

    public String getPipeLength() {
        return PipeLength;
    }

    public void setPipeLength(String pipeLength) {
        PipeLength = pipeLength;
    }

    public String getPipeWeight() {
        return PipeWeight;
    }

    public void setPipeWeight(String pipeWeight) {
        PipeWeight = pipeWeight;
    }

    public String getSurfaceCode() {
        return SurfaceCode;
    }

    public void setSurfaceCode(String surfaceCode) {
        SurfaceCode = surfaceCode;
    }

    public String getPipeType() {
        return PipeType;
    }

    public void setPipeType(String pipeType) {
        PipeType = pipeType;
    }
}
