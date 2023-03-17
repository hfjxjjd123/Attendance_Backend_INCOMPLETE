package com.proj252.AIstopwatch.proj252.service

import com.proj252.AIstopwatch.proj252.domain.Event
import com.proj252.AIstopwatch.proj252.domain.Group
import com.proj252.AIstopwatch.proj252.repository.SdjEventRepo
import com.proj252.AIstopwatch.proj252.repository.SdjGroupRepo
import com.proj252.AIstopwatch.proj252.repository.SdjRelatedGroupRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
//!! Service니까 날짜 변화시 초기화 등이 여기서 반영되어야 한다는 것. 각자마다 조회 & 날짜 변경 확인 & 이후 진행하는 로직을 만들 것.
class EventService {
    private var eventRepo: SdjEventRepo
    private var groupRepo: SdjGroupRepo
    private var relatedGroupRepo: SdjRelatedGroupRepo

    @Autowired
    //여기서 repo 갈아끼울 수 있음
    constructor(eventRepo: SdjEventRepo, groupRepo: SdjGroupRepo, relatedGroupRepo: SdjRelatedGroupRepo) {
        this.eventRepo = eventRepo
        this.groupRepo = groupRepo
        this.relatedGroupRepo = relatedGroupRepo
    }

    //TODO controller에서 Event를 받을 때 null이면 없다는 신호를 사용자에게 보내줘야한다.
    public fun getEventByGroup(groupId: Long): List<Event> {
        var event: List<Event>

        event = try {
            eventRepo.findEventByGroupId(groupId)

        } catch (e: Exception) {
            print("stopwatch get time err, retry?")
            listOf()
        }

        return event

    }

    //역할에 맞는 Group을 불러오는 로직
    public fun getGroupsByUser(uid: Long, role: Int): List<Long> {

        var groupsId: List<Long>

        //groupId를 얻어오고,
        //얻어 온 group_id로 조회해본 group이 존재하면 List에 눌러담기
        try {
            groupsId = relatedGroupRepo.findGroupIdByUserUidAndRole(uid, role)
        } catch (e: Exception) {
            groupsId = listOf<Long>()
            //TODO error handling
        }
        return groupsId
    }

    public fun getEventsByGroups(groups: List<Long>): List<Event> {
        var events = mutableListOf<Event>()

        try {
            for (groupId in groups) {
                //TODO 다른함수로 대체해야한다.
                events.add(eventRepo.findEventByGroupId(groupId)[0])
            }

        } catch (e: Exception) {
            //TODO error handling
        }

        return events
    }


}