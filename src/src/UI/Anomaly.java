package UI;

/**
 * Created by masterubunto on 23/06/18.
 */
public class Anomaly
{
   protected String Anomaly_type;
   protected String Anomaly_Description;



   public Anomaly (String anomaly_type,String anomaly_Description)
   {
       this.Anomaly_type=anomaly_type;
       this.Anomaly_Description=anomaly_Description;

   }



  public String toString ()
  {

      if(Anomaly_Description.isEmpty()) return "no Anomaly Spotted";
      return "Anomaly_type_Spotted:"+ this.Anomaly_type+"\t"+"Anomaly_Descprition:"+this.Anomaly_Description;

  }


}
