package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

public class MeteringData {
    private String documentID;
   private Interval interval;
   private Resolution resolution;
   private ConsumptionMeteringPoint consumptionMeteringPoint;
   private Product product;
   private List<Observation> observations;

    @XmlElement(name = "DocumentID", namespace = "http://www.strom.ch")
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
  @XmlElement(name="Interval", namespace = "http://www.strom.ch")
  public Interval getInterval() {
      return interval;
  }

  public void setInterval(Interval interval) {
      this.interval = interval;
  }
  @XmlElement(name="Resolution", namespace = "http://www.strom.ch")
  public Resolution getResolution() {
      return resolution;
  }

  public void setResolution(Resolution resolution) {
      this.resolution = resolution;
  }
  @XmlElement(name="ConsumptionMeteringPoint", namespace = "http://www.strom.ch")
  public ConsumptionMeteringPoint getConsumptionMeteringPoint() {
      return consumptionMeteringPoint;
  }

  public void setConsumptionMeteringPoint(ConsumptionMeteringPoint consumptionMeteringPoint) {
     this.consumptionMeteringPoint = consumptionMeteringPoint;
  }
  @XmlElement(name="Product", namespace = "http://www.strom.ch")
  public Product getProduct() {
      return product;
  }

  public void setProduct(Product product) {
      this.product = product;
  }
  @XmlElement(name="Observation", namespace = "http://www.strom.ch")
  public List<Observation> getObservations() {
      return observations;
  }

  public void setObservations(List<Observation> observations) {
      this.observations = observations;
  }

}
