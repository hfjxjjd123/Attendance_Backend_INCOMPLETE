package com.proj252.AIstopwatch.proj252.service

import com.proj252.AIstopwatch.proj252.domain.Event
import com.proj252.AIstopwatch.proj252.domain.Group
import com.proj252.AIstopwatch.proj252.domain.RelatedUser
import com.proj252.AIstopwatch.proj252.repository.SdjEventRepo
import com.proj252.AIstopwatch.proj252.repository.SdjGroupRepo
import com.proj252.AIstopwatch.proj252.repository.SdjRelatedGroupRepo
import com.proj252.AIstopwatch.proj252.repository.SdjRelatedUserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GroupService {
    private var eventRepo: SdjEventRepo
    private var groupRepo: SdjGroupRepo
    private var relatedGroupRepo: SdjRelatedGroupRepo
    private var relatedUserRepo: SdjRelatedUserRepo

    @Autowired
    //여기서 repo 갈아끼울 수 있음
    constructor(
        eventRepo: SdjEventRepo,
        groupRepo: SdjGroupRepo,
        relatedGroupRepo: SdjRelatedGroupRepo,
        relatedUserRepo: SdjRelatedUserRepo
    ) {
        this.eventRepo = eventRepo
        this.groupRepo = groupRepo
        this.relatedGroupRepo = relatedGroupRepo
        this.relatedUserRepo = relatedUserRepo
    }

    //TODO controller에서 Event를 받을 때 null이면 없다는 신호를 사용자에게 보내줘야한다.
    public fun getEvents(groupId: Long): List<Event> {

        return try {
            eventRepo.findAllByGroupId(groupId)
        } catch (e: Exception) {
            print("stopwatch get time err, retry?")
            listOf()
        }

    }

    public fun createGroup(userId: Long, groupName: String){

        //TODO 부분적 초기화해야함
        var group: Group =
            Group(name = groupName, notification = "")

        val relatedUser: RelatedUser = RelatedUser(userId, 2, null, null, group)

        groupRepo.save(group)
        relatedUserRepo.save(relatedUser)

    }

    public fun getNotification(gid: Long): String {
        var notification: String

        try {
            notification = groupRepo.getNotification(gid) ?: ""
        } catch (e: Exception) {
            notification = ""
            //TODO error handling
        }
        return notification
    }

    public fun getGroup(gid: Long): Group? {
        val group: Group?

        group = groupRepo.getGroupById(gid)

        if (group == null) {
            print("serious error")
        }

        return group
    }
}