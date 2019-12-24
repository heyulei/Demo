import java.util.*;
 class TreeNode {
    int pro;
    ArrayList<Integer> Max = null;
    ArrayList<Integer> allo = null;
    ArrayList<Integer> need = null;
    Boolean Finish =false;

    public TreeNode(int pro) {
        this.pro = pro;

    }

}
public class Banker {
    public static void main(String[] args) {
        int proNum;//进程数
        int recNum;//资源种类数
        int allRecNum;//资源总类数
//        ArrayList allo = new ArrayList();//Allocations
//        ArrayList need = new ArrayList();//need
        ArrayList<Integer> avai = new ArrayList();//Availiable
//        TreeNode tmp = new TreeNode(0);
        ArrayList<TreeNode> pAll = new ArrayList<>();//p0.p1.p2...
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入进程数");
        proNum = sc.nextInt();
        System.out.println("请输入资源种类数");
        recNum = sc.nextInt();
        inPutInfo(pAll,avai,proNum,recNum);
        menu(pAll,avai,proNum,recNum);
    }

    private static void inPutInfo( ArrayList<TreeNode> pAll,
                                   ArrayList<Integer> avai, int proNum, int recNum) {
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入Availiable");
        for (int j = 0; j < recNum; j++) {
            avai.add(sc.nextInt());
        }


        for (int i = 0; i < proNum; i++) {
            ArrayList<Integer> alo = new ArrayList();
            ArrayList<Integer> ned = new ArrayList();
            ArrayList<Integer> max = new ArrayList();
            System.out.println("请输入p"+ i +"max");
            for (int j = 0; j < recNum; j++) {
                max.add(sc.nextInt());
            }
            System.out.println("请输入p"+ i +"Allocations");
            for (int k = 0; k < recNum; k++) {
                alo.add(sc.nextInt());
            }
            System.out.println("请输入p"+ i +"Need");
            for (int l = 0; l < recNum; l++) {
                ned.add(sc.nextInt());
            }
            TreeNode tmp = new TreeNode(i );
            tmp.Max = max;
            tmp.allo = alo;
            tmp.need = ned;
            pAll.add(tmp);
        }
        System.out.println("录入完成");
       // display(pAll);
        return;
    }
    private static void menu(ArrayList <TreeNode>pAll,ArrayList<Integer> avai,int proNum,int recNum){
        System.out.println("请输入要执行的操作");
        System.out.println("--------------------------------------");
        System.out.println("-----------1.安全性检测---------------");
        System.out.println("-----------2.银行家算法检查-----------");
        System.out.println("-----------0.退出---------------------");
        System.out.println("--------------------------------------");
        int choice;
        boolean exit = false;
        while (!exit) {
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            switch (choice){
                case 1:safeCheck(pAll,avai,proNum,recNum);
                break;
                case 2:
                    System.out.println("请输入要分配资源进程的编号");
                    int proid = sc.nextInt();
                    System.out.println("请输入要分配的资源");
                    ArrayList<Integer> Req = new ArrayList<>();
                    for(int i = 0;i<recNum;i++){
                        Req.add(sc.nextInt());
                    }
                    banksAlgorithmCheck(pAll,avai,proNum,recNum,proid,Req);
                    break;
                case 3:exit = true;
                break;
            }
        }
    }
    private static int safeCheck(ArrayList<TreeNode> pAll,ArrayList<Integer> avai,int proNum,int recNum){
        ArrayList<Integer> work = new ArrayList<>();
        ArrayList<TreeNode> setpAll = pAll;
        copy(avai,work);
        ArrayList<Integer>safeOrder = new ArrayList<>();
        int count = 0;
        int breakCount = 0;
        while (count<proNum){
            if (breakCount-count>1){
                return 0;
            }
            for (int j = 0; j <proNum; j++) {
                if (setpAll.get(j).Finish)
                    continue;
                int num = setpAll.get(j).pro;
                ArrayList<Integer> tempwork =  new ArrayList<>();
                for (int i = 0; i <recNum ; i++) {
                    if (work.get(i)>=setpAll.get(num).need.get(i)){
                        tempwork.add(work.get(i)+setpAll.get(num).allo.get(i));
                        if (i == recNum-1){
                            work = tempwork;
                            setpAll.get(j).Finish = true;
                            count++;
                            safeOrder.add(j);
                        }
                    }else{
                        break;
                    }
                }
            }
            breakCount++;
            for (int i = 0; i <proNum ; i++) {
                setpAll.get(i).Finish = false;
            }
        }
        if (count>=proNum){
            System.out.println("存在安全序列");
            System.out.print("安全序列为：");
            for (int i = 0;i<safeOrder.size();i++){
                System.out.print(safeOrder.get(i)+", ");
                return 1;
            }
        }
        System.out.println("无安全序列");return 0;
    }
    private static void copy(ArrayList<Integer> src,ArrayList<Integer> dist){
        for (int i = 0; i <src.size() ; i++) {
            dist.add(src.get(i));
        }
        return;
    }
    private static int banksAlgorithmCheck(
            ArrayList<TreeNode> pAll,ArrayList<Integer> avai,
            int proNum,int recNum,int proid,ArrayList<Integer> Req){
        for (int i = 0; i <recNum ; i++) {
            if (Req.get(i)>pAll.get(i).need.get(i)){
                return -1;
            }
            if (Req.get(i)>pAll.get(i).allo.get(i)){
                return 0;
            }
            ArrayList<Integer> tempavil = new ArrayList<>();
            ArrayList<TreeNode>temppAll = new ArrayList<>();
            copypAll(pAll,temppAll,Req,proid);
            for (int j = 0;j<recNum;j++){
                tempavil.add(avai.get(i)-Req.get(i));
            }
            int ret = safeCheck(temppAll,tempavil,proNum,recNum);
            if (ret == 1){
                pAll = temppAll;
                avai = tempavil;
                return 1;
            }
        }
        return 0;
    }
    private static void copypAll(ArrayList<TreeNode> src,ArrayList<TreeNode> dist,ArrayList <Integer>Req,int proid ){
        for (int i = 0; i <src.size() ; i++) {
            dist.add(src.get(i));
            if (proid != i){
                dist.get(i).allo.add(src.get(i).allo.get(i));
                dist.get(i).need.add(src.get(i).need.get(i));
            }else {
                dist.get(i).allo.add(src.get(i).allo.get(i)+Req.get(i));
                dist.get(i).need.add(src.get(i).need.get(i)-Req.get(i));
            }
            dist.get(i).Max.add(src.get(i).need.get(i));
            dist.get(i).pro = src.get(i).pro;
        }
        return;
    }
}

