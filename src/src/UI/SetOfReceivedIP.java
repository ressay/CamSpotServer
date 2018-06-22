package UI;

import java.util.ArrayList;

/**
 * Created by masterubunto on 22/06/18.
 */
public class SetOfReceivedIP
{

public ArrayList <ReceivedIP> DataBaseof_FR; /*frame received*/

    public SetOfReceivedIP()
    {

    DataBaseof_FR=new ArrayList<>();
    }

    public void add (ReceivedIP rp)
    {

        this.DataBaseof_FR.add(rp);


    }
    public  ReceivedIP get (int i)
    {


        return        this.DataBaseof_FR.get(i);
    }



    public ReceivedIP Look_up_Frame (String ipAddress)
    {

        for (int i=0; i<this.DataBaseof_FR.size();i++)
        {
            System.out.println("found");
            if(DataBaseof_FR.get(i).getIpAddress().compareTo(ipAddress)==0)
                return  DataBaseof_FR.get(i);

        }


    return null;

    }
}