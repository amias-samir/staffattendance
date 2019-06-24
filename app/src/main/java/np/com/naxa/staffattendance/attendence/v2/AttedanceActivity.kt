package np.com.naxa.staffattendance.attendence.v2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_dashboard_attedance.*
import np.com.naxa.staffattendance.R
import np.com.naxa.staffattendance.StaffListAdapter
import np.com.naxa.staffattendance.attedancedashboard.ItemOffsetDecoration
import np.com.naxa.staffattendance.attendence.TeamMemberResposne
import np.com.naxa.staffattendance.common.BaseActivity
import np.com.naxa.staffattendance.common.IntentConstants
import np.com.naxa.staffattendance.database.AttendanceDao
import np.com.naxa.staffattendance.database.StaffDao
import np.com.naxa.staffattendance.database.TeamDao
import np.com.naxa.staffattendance.utlils.DateConvertor
import java.sql.Date

class AttedanceActivity : BaseActivity(), StaffListAdapter.OnStaffItemClickListener {
    override fun onStaffClick(pos: Int, staff: TeamMemberResposne?) {
    }

    override fun onStaffLongClick(pos: Int) {
    }

    private var loadedDate: String? = null;
    private lateinit var stafflistAdapter: StaffListAdapter
    private var teamDao: TeamDao? = null
    private var enablePersonSelection = true;
    private var attedanceIds: List<String>? = emptyList()
    private lateinit var teamId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_attedance)
        loadedDate = intent.getStringExtra(IntentConstants.ATTENDANCE_DATE);
        teamId = intent.getStringExtra(IntentConstants.TEAM_ID);

//        val dailyAttendance = AttendanceDao().getAttedanceByDate(teamId, loadedDate)
//        setAttendanceIds(dailyAttendance.presentStaffIds,dailyAttendance.getAttendanceDate(false))

        setupToolbar(title = "Team Name")
        setupRecyclerView()

    }

    private fun setupToolbar(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
        tv_screen_name.text = title

    }



    private fun setupRecyclerView() {
        val teamDao = TeamDao();
        val mLayoutManager = LinearLayoutManager(getApplicationContext())
        val teamId = teamDao.oneTeamIdForDemo

        val staffs = StaffDao().getStaffByTeamId(teamId)
        stafflistAdapter = StaffListAdapter(this, staffs, enablePersonSelection, attedanceIds, this)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = stafflistAdapter
        recycler_view.addItemDecoration(ItemOffsetDecoration(this, R.dimen.spacing_small))
    }

    fun setAttendanceIds(attendanceIds: List<String>?, attendanceDate: String) {
        this.attedanceIds = attendanceIds
        val isAttedanceEmpty = attendanceIds?.isEmpty()
        val isAttedanceDateToday = DateConvertor.getCurrentDate().equals(attendanceDate, ignoreCase = true)
        if (isAttedanceEmpty!! && isAttedanceDateToday) {
            enablePersonSelection = true
        }

        val isAttendenceNotEmpty = !isAttedanceEmpty

        if (isAttedanceDateToday && isAttendenceNotEmpty) {
            enablePersonSelection = false
        }
    }


    companion object {
        fun newIntent(context: Context, date: String,teamId: String): Intent {
            val intent = Intent(context, AttedanceActivity::class.java)
            intent.putExtra(IntentConstants.ATTENDANCE_DATE, date);
            intent.putExtra(IntentConstants.TEAM_ID, teamId);
            return intent
        }
    }

}