package com.example.myapplication

//아이디 얻기
interface AnimalIDListener {
    fun animalFromID(animalID: Int): Animal? {
        for(animal in animalList){
            if(animal.id == animalID)
                return animal
        }
        return null
    }
    fun scheduleFromID(scheduleID: Int): Schedule? {
        for(schedule in scheduleList){
            if(schedule.id == scheduleID)
                return schedule
        }
        return null
    }
    fun inoculationFromID(inoculationID: Int): Inoculation? {
        for(inoculation in inoculationList){
            if(inoculation.id == inoculationID)
                return inoculation
        }
        return null
    }
}