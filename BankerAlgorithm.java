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

        return;
    }
    private static void menu(ArrayList <TreeNode>pAll, ArrayList<Integer> avai, int proNum, int recNum){
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
                    display(pAll,proNum,recNum,avai);
                    break;
                case 2:

                    int ret = banksAlgorithmCheck(pAll,avai,proNum,recNum);
                    if (ret < 0 ){
                        System.out.println("所需资源数超过宣布的最大值");
                    }else if(ret ==0){
                        System.out.println("无足够资源");
                    }else if(ret == 1){
                        System.out.println("分配成功");
                    }
                    else{
                        System.out.println("分配失败");
                    }
                    display(pAll,proNum,recNum,avai);
                    break;
                case 0:exit = true;
                    break;
            }
        }
    }
    private static int safeCheck(ArrayList<TreeNode> pAll, ArrayList<Integer> avai, int proNum, int recNum){
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

        }
        for (int i = 0; i <proNum ; i++) {
            setpAll.get(i).Finish = false;
        }
        if (count>=proNum){
            System.out.println("存在安全序列");
            System.out.print("安全序列为：");
            for (int i = 0;i<safeOrder.size();i++){
                System.out.print(safeOrder.get(i)+", ");

            }
            System.out.println("");
            return 1;
        }
        //      3 2
//        8 6
//        0 2
//        7 5
//        6 4
//        4 2
//        3 0
//        10 8 p1 22
//                p0  21
        System.out.println("无安全序列");
        return 0;
    }
    private static void copy(ArrayList<Integer> src,ArrayList<Integer> dist){
        for (int i = 0; i <src.size() ; i++) {
            dist.add(src.get(i));
        }
        return;
    }
    private static int banksAlgorithmCheck(ArrayList<TreeNode> pAll, ArrayList<Integer> avai,
                                           int proNum, int recNum){
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入要分配资源进程的编号");
        int proid = sc.nextInt();
        System.out.println("请输入要分配的资源");
        ArrayList<Integer> Req = new ArrayList<>();
        for(int i = 0;i<recNum;i++){
            Req.add(sc.nextInt());
        }
        for (int i = 0; i <recNum ; i++) {
            if (Req.get(i)>pAll.get(proid).need.get(i)){
                return -1;
            }

        }
        for (int i = 0; i <recNum ; i++) {
            if (Req.get(i)>avai.get(i)){
                return 0;
            }
        }
        ArrayList<Integer> tempavil = new ArrayList<>();
        ArrayList<TreeNode>temppAll = new ArrayList<>();
        copypAll(pAll,temppAll,Req,proid,proNum,recNum);
        for (int j = 0;j<recNum;j++){
            tempavil.add(avai.get(j)-Req.get(j));
        }
        int ret = safeCheck(temppAll,tempavil,proNum,recNum);
        if (ret == 1){
            for (int i = 0; i <proNum ; i++) {
                pAll.set(i,temppAll.get(i));
                if (i<recNum) {
                    avai.set(i,tempavil.get(i));
                }

            }
            return 1;
        }else{
            return 2;
        }


    }
    private static void copypAll(ArrayList<TreeNode> src, ArrayList<TreeNode> dist, ArrayList <Integer>Req, int proid , int proNum, int recNum){
        for (int i = 0; i <proNum ; i++) {
            TreeNode tmp = new TreeNode(i);

            ArrayList<Integer> allo = new ArrayList<>();
            ArrayList<Integer> need = new ArrayList<>();
            ArrayList<Integer> Max = new ArrayList<>();

            for (int j = 0; j <recNum; j++) {
                if (proid != i){
                    allo.add(src.get(i).allo.get(j));
                    need.add(src.get(i).need.get(j));
                }else {
                    allo.add(src.get(i).allo.get(j)+Req.get(j));
                    need.add(src.get(i).need.get(j)-Req.get(j));
                }
                Max.add(src.get(i).need.get(j));

            }tmp.Max = Max;
            tmp.allo = allo;
            tmp.need = need;
            tmp.pro = i;
            dist.add(tmp);
        }
        return;
    }
    private static void display(ArrayList<TreeNode>pAll, int proNum, int recNum, ArrayList<Integer>avai){
        System.out.println("进程—————Max—————Allocations—————Need——————");
        for (int m = 0; m <recNum ; m++) {
            System.out.print(avai.get(m)+",");
        }
        System.out.println("");
        for (int i = 0; i <proNum ; i++) {
            System.out.print(pAll.get(i).pro+"—————|");
            for (int j = 0; j <recNum ; j++) {
                System.out.print(pAll.get(i).Max.get(j)+"—————");
            }
            for (int k = 0; k <recNum ; k++) {
                System.out.print(pAll.get(i).allo.get(k)+"—————");
            }
            for (int l = 0; l <recNum ; l++) {
                System.out.print(pAll.get(i).need.get(l)+"—————");
            }
            System.out.println("");
        }
    }

}
