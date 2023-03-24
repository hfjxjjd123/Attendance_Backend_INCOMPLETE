package com.proj252.AIstopwatch.proj252.controller

import com.proj252.AIstopwatch.proj252.domain.Event
import com.proj252.AIstopwatch.proj252.domain.Group
import com.proj252.AIstopwatch.proj252.dto.event.EventDto
import com.proj252.AIstopwatch.proj252.dto.group.*
import com.proj252.AIstopwatch.proj252.service.EventService
import com.proj252.AIstopwatch.proj252.service.GroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class GroupController {

    private final var eventService: EventService
    private final var groupService: GroupService

    @Autowired
    constructor(eventService: EventService, groupService: GroupService) {
        this.eventService = eventService
        this.groupService = groupService
    }

    @GetMapping("{gid}/events")
    @ResponseBody
    fun getGroupEvents(@PathVariable gid: Long): List<Event> {

        return groupService.getEvents(gid)

        //통신시 List를 통째로 보내고 client-side에서 해당 메시지(Stinrg)을 객체로 파싱하는 과정을 거쳐야한다.
    }


    @GetMapping("{gid}")
    @ResponseBody
    fun getGroup(@PathVariable gid: Long): ResponseEntity<Any> {

        var groupDto: GroupDto? = null

        val group: Group? = groupService.getGroup(gid)
        group?.let {
            val event: Event? = eventService.getRecentEventByGroup(gid)
            groupDto = GroupDto(group.id, group.name, group.notification ?: "", event)
        }

        return if (groupDto != null) {
            ResponseEntity.ok(groupDto)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group not found")
        }
    }

    @PostMapping("create-group")
    @ResponseBody
    fun createGroup(@RequestBody groupCreateDto: GroupCreateDto): ResponseEntity<String> {

        try {
            groupService.createGroup(groupCreateDto.userId, groupCreateDto.name, groupCreateDto.username)
            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed")
        }
    }

    @PostMapping("{gid}/join")
    @ResponseBody
    fun addUser(@PathVariable gid: Long, @RequestBody groupJoinDto: GroupJoinDto): ResponseEntity<String> {

        try {
            groupService.addUser2Group(gid, groupJoinDto.userId, groupJoinDto.username)
            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed")
        }
    }

    @PostMapping("{gid}/remove")
    @ResponseBody
    fun removeUser(@PathVariable gid: Long, @RequestBody uid: Long): ResponseEntity<String> {

        try {
            groupService.removeUser2Group(gid, uid)
            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed")
        }
    }


    //여기서 부턴 Event와 연관되어있습니다.

    @GetMapping("{gid}/recent-event")
    @ResponseBody
    fun getRecentGroupEvent(@PathVariable gid: Long): Event? {

        return eventService.getRecentEventByGroup(gid)
    }


    @PostMapping("{gid}/create-event")
    @ResponseBody
    fun createEvent(@PathVariable gid: Long, @RequestBody eventDto: EventDto): ResponseEntity<String> {

        try {
            eventService.createEvent(eventDto, gid)
            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed")
        }
    }

    @PostMapping("{gid}/change-event")
    @ResponseBody
    fun modifyEvent(@PathVariable gid: Long, @RequestBody eventDto: EventDto): ResponseEntity<String> {

        try {
            eventService.modifyEvent(eventDto)
            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed")
        }
    }

    @PostMapping("{gid}/delete-event")
    @ResponseBody
    fun deleteEvent(@PathVariable gid: Long, @RequestBody eventId: Long): ResponseEntity<String> {

        try {
            eventService.deleteEvent(eventId)
            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed")
        }
    }

    @GetMapping("{gid}/my-attends")
    @ResponseBody
    fun getMyAttends(@PathVariable gid: Long, @RequestBody uid: Long): Int {
        return groupService.getMyAttends(gid, uid)
    }
    @GetMapping("{gid}/group-attends")
    @ResponseBody
    fun getAttends(@PathVariable gid: Long): List<GroupUserAttendsDto> {
        return groupService.getGroupAttends(gid)
    }
    @GetMapping("{gid}/attends-now")
    @ResponseBody
    fun getAttendsNow(@PathVariable gid: Long): List<GroupUserAttendDto> {
        return groupService.getGroupAttendsNow(gid)

    }
}