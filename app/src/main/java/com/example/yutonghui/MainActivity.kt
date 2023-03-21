package com.example.yutonghui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.yutonghui.ui.theme.YuTongHuiTheme

//UI分为4行 标题区TitleArea，等待区WaitArea，对战区BattleArea，休息区RestArea

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YuTongHuiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
data class Member(var serial_number: Int, var name: String)

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen(){
    val title = "羽同会-乱数器beta"
    var new_member_name by remember{ mutableStateOf("") }
    val game_list = remember { mutableStateListOf<Member>(
        Member(serial_number = -1, name = "XXX-1"),
        Member(serial_number = -2, name = "XXX-2"),
        Member(serial_number = -3, name = "XXX-3"),
        Member(serial_number = -4, name = "XXX-4")) }
    val wait_list = remember{mutableStateListOf<Member>(
        Member(serial_number = 1, name = "王"),
        Member(serial_number = 2, name = "赵"),
        Member(serial_number = 3, name = "钱"),
        Member(serial_number = 4, name = "郑"),
        Member(serial_number = 5, name = "王"),
        Member(serial_number = 6, name = "赵"),
        Member(serial_number = 7, name = "钱"),
        Member(serial_number = 8, name = "郑"),
        Member(serial_number = 9, name = "王"),
        Member(serial_number = 10, name = "赵"),
        Member(serial_number = 11, name = "赵"),
        Member(serial_number = 12, name = "赵"),
        Member(serial_number = 13, name = "钱"))}
    val wait_list_up = remember{mutableStateListOf<Member>()}
    val wait_list_upup = remember{mutableStateListOf<Member>()}
    var total_number by remember{ mutableStateOf(wait_list.size) }
    val rest_list = remember{mutableStateListOf<Member>()}
    var open_dialog_flag by remember{ mutableStateOf(false) }
    var alert_dialog_flag by remember{mutableStateOf(false)}
    var alert_dialog_flag_2 by remember{mutableStateOf(false)}
    var modify_dialog_flag by remember{ mutableStateOf(false) }
    var selected_member by remember{ mutableStateOf(Member(serial_number = 0, name = "None")) }
    var selected_member_name by remember{ mutableStateOf("") }
    var selected_area by remember{mutableStateOf("Unknown Area")}

    if(open_dialog_flag){
        AlertDialog(
            onDismissRequest = {
                open_dialog_flag = false
            },
            title = {
                Text("添加新成员")
            },
            text = {
                LazyColumn() {
                    item{
                        Text("序号：${total_number+1}")
                        TextField(value = new_member_name,
                            onValueChange = {new_member_name = it},
                            label = { Text("姓名") },
                            placeholder = { Text("请输入姓名") })
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        open_dialog_flag = false
                        //添加新成员
                        wait_list.add(Member(serial_number = total_number + 1,name = new_member_name))
                        total_number = total_number + 1
                        new_member_name = ""
                    }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        open_dialog_flag = false
                        //取消添加新成员
                        new_member_name = ""
                    }) {
                    Text("取消")
                }
            }
        )
    }
    if(alert_dialog_flag){
        AlertDialog(
            onDismissRequest = {
                alert_dialog_flag = false
            },
            title = {
                Text("警告！")
            },
            text = {
                Text("人数不足4人，不能开始！")
            },
            confirmButton = { TextButton(onClick = { alert_dialog_flag = false }) { Text("确定") }
            },
        )
    }
    if(alert_dialog_flag_2){
        AlertDialog(
            onDismissRequest = {
                alert_dialog_flag_2 = false
            },
            title = {
                Text("警告！")
            },
            text = {
                Text("被删除人在对战，不能删除！")
            },
            confirmButton = { TextButton(onClick = { alert_dialog_flag_2 = false }) { Text("确定") }
            },
        )
    }
    if(modify_dialog_flag){
        AlertDialog(
            onDismissRequest = {
                modify_dialog_flag = false
            },
            title = {
                Text("修改/删除成员")
            },
            text = {
                LazyColumn() {
                    item{
                        Text("区域：${selected_area}")
                        Text("序号：${selected_member.serial_number}")
                        TextField(value = selected_member_name,
                            onValueChange = {selected_member_name = it},
                            label = { Text("姓名") },
                            placeholder = { Text("请输入姓名") })

                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        modify_dialog_flag = false
                        //修改成员信息
                        when(selected_area){
                            "Wait Area" ->{ wait_list.set(wait_list.indexOf(selected_member),Member(serial_number = selected_member.serial_number,name = selected_member_name)) }
                            "Wait Area[UP]" ->{ wait_list_up.set(wait_list_up.indexOf(selected_member),Member(serial_number = selected_member.serial_number,name = selected_member_name)) }
                            "Wait Area[UPUP]" ->{ wait_list_upup.set(wait_list_upup.indexOf(selected_member),Member(serial_number = selected_member.serial_number,name = selected_member_name)) }
                            "Rest Area" ->{ rest_list.set(rest_list.indexOf(selected_member),Member(serial_number = selected_member.serial_number,name = selected_member_name)) }
                            else->{}
                        }
                        selected_member = Member(serial_number = 0, name = "None")
                        selected_member_name = ""
                    }) {
                    Text("修改成员")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        modify_dialog_flag = false
                        //删除成员
                        //注意，如果是对战中的成员是不能删除的！
                        if (game_list.contains(selected_member)){
                            //不准删除并弹窗警告
                            alert_dialog_flag_2 = true
                        }else{
                            var deleted_serial_num = selected_member.serial_number
                            when(selected_area){
                                "Wait Area" ->{ wait_list.removeAt(wait_list.indexOf(selected_member)) }
                                "Wait Area[UP]" ->{ wait_list_up.removeAt(wait_list_up.indexOf(selected_member)) }
                                "Wait Area[UPUP]" ->{ wait_list_upup.removeAt(wait_list_upup.indexOf(selected_member)) }
                                "Rest Area" ->{ rest_list.removeAt(rest_list.indexOf(selected_member)) }
                                else->{}
                            }
                            //更新序列号
                            for(member in game_list){ if (member.serial_number>deleted_serial_num){member.serial_number --} }
                            for(member in wait_list){ if (member.serial_number>deleted_serial_num){member.serial_number --} }
                            for(member in wait_list_up){ if (member.serial_number>deleted_serial_num){member.serial_number --} }
                            for(member in wait_list_upup){ if (member.serial_number>deleted_serial_num){member.serial_number --} }
                            for(member in rest_list){ if (member.serial_number>deleted_serial_num){member.serial_number --} }
                            total_number--
                        }
                        selected_member = Member(serial_number = 0, name = "None")
                        selected_member_name = ""
                    }) {
                    Text("删除成员")
                }
            }
        )
    }
    Scaffold(
        modifier = Modifier.padding(12.dp),
        topBar = { CenterAlignedTopAppBar(title = {Text("$title")}, colors = TopAppBarDefaults.centerAlignedTopAppBarColors())  },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = { open_dialog_flag = true },){
            Icon(Icons.Filled.Person, contentDescription = "Add")
        } },
        content = { padding -> Column(modifier = Modifier
            .padding(padding)
            .border(1.dp, color = Color.Blue)
            .fillMaxSize()
        ) {
            // 纵向布局4个区域，1-对战区，2-等待区，3-休息区
            Column(verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Top),
                modifier = Modifier
                    .fillMaxSize()) {
                // 1-对战区
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            3.dp,
                            color = MaterialTheme.colorScheme.error,
                            shape = RectangleShape
                        )
                        .padding(6.dp))
                {
                    //对战者L1
                    Card(colors = CardDefaults.cardColors(), modifier = Modifier.padding(3.dp)) {
                        LazyRow(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(6.dp)) {
                            item {
                                Text("${game_list[0].serial_number}",fontWeight = FontWeight.Black)
                                Text("${game_list[0].name}",fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                    Text("+",fontWeight = FontWeight.Black)
                    //对战者L2
                    Card(colors = CardDefaults.cardColors(), modifier = Modifier.padding(3.dp)) {
                        LazyRow(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(6.dp)) {
                            item {
                                Text("${game_list[1].serial_number}",fontWeight = FontWeight.Black)
                                Text("${game_list[1].name}",fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                    Text("V.S.",fontWeight = FontWeight.Black)
                    //对战者R1
                    Card(colors = CardDefaults.cardColors(), modifier = Modifier.padding(3.dp)) {
                        LazyRow(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(6.dp)) {
                            item {
                                Text("${game_list[2].serial_number}",fontWeight = FontWeight.Black)
                                Text("${game_list[2].name}",fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                    Text("+",fontWeight = FontWeight.Black)
                    //对战者R2
                    Card(colors = CardDefaults.cardColors(), modifier = Modifier.padding(3.dp)) {
                        LazyRow(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(6.dp)) {
                            item {
                                Text("${game_list[3].serial_number}",fontWeight = FontWeight.Black)
                                Text("${game_list[3].name}",fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                }

                // 2-等待区
                LazyVerticalGrid(columns = GridCells.Adaptive(80.dp),modifier = Modifier
                    .border(3.dp, color = MaterialTheme.colorScheme.primary)
                    .padding(6.dp)
                    .fillMaxWidth()){
                    item{
                        Card(colors = CardDefaults.cardColors(), modifier = Modifier.padding(3.dp), shape = RectangleShape) {
                            Text("等待区",fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(6.dp))
                        }
                    }
                    items(wait_list_upup.size){
                            index ->
                        Card(colors = CardDefaults.cardColors(),
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable(
                                    onClick = {
                                        modify_dialog_flag = true;
                                        selected_area = "Wait Area[UPUP]";
                                        selected_member = wait_list_upup[index];
                                        selected_member_name = selected_member.name
                                    })
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                                .padding(6.dp)
                                .border(1.dp, color = MaterialTheme.colorScheme.error)) {
                                Text("${wait_list_upup[index].serial_number}",fontWeight = FontWeight.Black)
                                Text("  ${wait_list_upup[index].name}",fontWeight = FontWeight.Normal)
                                Text("↑↑",fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    items(wait_list_up.size){
                            index ->
                        Card(colors = CardDefaults.cardColors(),
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable(
                                    onClick = {
                                        modify_dialog_flag = true;
                                        selected_area = "Wait Area[UP]";
                                        selected_member = wait_list_up[index];
                                        selected_member_name = selected_member.name
                                    })
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                                .padding(6.dp)
                                .border(1.dp, color = MaterialTheme.colorScheme.primary)) {
                                Text("${wait_list_up[index].serial_number}",fontWeight = FontWeight.Black)
                                Text("  ${wait_list_up[index].name}",fontWeight = FontWeight.Normal)
                                Text("↑",fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    items(wait_list.size){
                            index ->
                        Card(colors = CardDefaults.cardColors(),
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable(
                                    onClick = {
                                        modify_dialog_flag = true;
                                        selected_area = "Wait Area";
                                        selected_member = wait_list[index];
                                        selected_member_name = selected_member.name
                                    })
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(6.dp)) {
                                Text("${wait_list[index].serial_number}",fontWeight = FontWeight.Black)
                                Text("  ${wait_list[index].name}",fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                }
                // 3-休息区
                LazyVerticalGrid(columns = GridCells.Adaptive(80.dp),modifier = Modifier
                    .border(3.dp, color = MaterialTheme.colorScheme.secondary)
                    .padding(6.dp)
                    .fillMaxWidth()){
                    item{
                        Card(colors = CardDefaults.cardColors(), modifier = Modifier.padding(3.dp), shape = RectangleShape) {
                            Text("休息区",fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(6.dp))
                        }
                    }
                    items(rest_list.size){
                            index ->
                        Card(colors = CardDefaults.cardColors(),
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable(
                                    onClick = {
                                        modify_dialog_flag = true;
                                        selected_area = "Rest Area";
                                        selected_member = rest_list[index];
                                        selected_member_name = selected_member.name
                                    })
                                ) {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(6.dp)) {
                                Text("${rest_list[index].serial_number}",fontWeight = FontWeight.Black)
                                Text("  ${rest_list[index].name}",fontWeight = FontWeight.Normal)
                            }

                        }

                    }

                }
            }
        } },
        bottomBar = { BottomAppBar(contentPadding = PaddingValues(6.dp)) {
            Button(
                onClick = {
                    if(total_number >= 4){
                        // 总人数大于等于4，才可以使用
                        if(game_list[0].serial_number<0){
                            //第1场
                            game_list.clear()
                        } else{
                            //不是第1场
                            //注意如果是UP的人的话，已经在wait了所以不能加入到rest
                            for (gamer in game_list){
                                if(!(wait_list.contains(gamer)||wait_list_up.contains(gamer)||wait_list_upup.contains(gamer))){
                                    rest_list.add(gamer)
                                }
                            }
                            game_list.clear()
                            rest_list.sortBy { member -> member.serial_number }
                        }
                        var need_num = 4
                        if(wait_list_upup.size != 0){
                            //UPUP的人 必须加入
                            need_num = need_num - wait_list_upup.size
                            game_list.addAll(wait_list_upup)
                            wait_list_upup.clear()
                        }
                        if(need_num>0){
                            //如果还差人的话
                            if((wait_list.size+wait_list_up.size)<=4){
                                //需要概率UP
                                wait_list_upup.addAll(wait_list_up)
                                wait_list_up.clear()
                                wait_list_up.addAll(wait_list)
                                wait_list.clear()
                                wait_list.addAll(rest_list)
                                rest_list.clear()
                            }
                            //从wait和wait up里面选人
                            for (index in 0 until need_num){
                                var random_num = (1..(wait_list.size+2*wait_list_up.size)).shuffled().last()
                                if(random_num<=wait_list.size){
                                    //在wait_list里选人
                                    //注意这里有可能选到上一个循环里从uplist转移到这个list的人
                                    while(true){
                                        //选择的人和对战的人重复，就从新选人
                                        val new_gamer = wait_list.random()
                                        if(!game_list.contains(new_gamer)){
                                            game_list.add(new_gamer)
                                            wait_list.remove(new_gamer)
                                            break
                                        }
                                    }
                                    
                                }else{
                                    //在wait_list_up里选人
                                    var new_gamer = wait_list_up.random()
                                    game_list.add(new_gamer)
                                    wait_list_up.remove(new_gamer)
                                    wait_list.add(new_gamer)
                                    wait_list.sortBy { member -> member.serial_number }
                                }
                            }
                        }
                        //旧的逻辑在8人下有BUG
//                        //如果waitlist人数少于等于4，waitlist直接进gamelist，waitlist清空，赋值restlist给waitlist，清空restlist
//                        if(wait_list.size<=4){
//                            game_list.addAll(wait_list)
//                            wait_list.clear()
//                            wait_list.addAll(rest_list)
//                            rest_list.clear()
//                        }
//                        //计算还差need_num个人，waitlist_temp乱序，pop出need_num个人到gamelist，gamelist乱序，排序waitlist
//                        var need_num = 4 - game_list.size
//                        for(index in 0 until need_num){
//                            var new_member = wait_list.random()
//                            wait_list.remove(new_member)
//                            game_list.add(new_member)
//                        }
                        game_list.shuffle()
                     }else{
                        // 总人数小于4，不可以使用
                        alert_dialog_flag = true
                     }
                },
                modifier = Modifier.fillMaxWidth()){
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "New Game"
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "新的一局")
        } } }
    )
}


