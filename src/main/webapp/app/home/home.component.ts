import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import {Account, AccountService, LoginModalService, Principal, User} from '../shared';
import {RfbLocation, RfbLocationService} from '../entities/rfb-location';
import {RfbEvent, RfbEventService} from '../entities/rfb-event';
import {RfbEventAttendance, RfbEventAttendanceService} from '../entities/rfb-event-attendance';
import {HttpResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})

export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    isSaving: boolean;
    locations: RfbLocation[];
    todaysEvent: RfbEvent;
    currentUser: User;
    model: any;
    rfbEventAttendance: RfbEventAttendance;
    errors: any = {invalidEventCode: ''};
    checkedIn: any;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private locationService: RfbLocationService,
        private eventService: RfbEventService,
        private accountService: AccountService,
        private rfbEventAttendanceService: RfbEventAttendanceService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
            this.afterUserAccountSetup(account);
        });
        this.registerAuthenticationSuccess();
        this.loadLocations();
        this.model = {location: 0, eventCode: ''};
        this.rfbEventAttendance = new RfbEventAttendance(null, new Date(), null, null);

        // get current User; if it is of role organizer show todays event for their location
        // this.accountService.get().subscribe((user: User) => {

            // this.currentUser = user;
            // this.rfbEventAttendance.rfbUserId = user.id;
            // this.rfbEventAttendance.rfbUserId = this.currentUser.id;
            //
            // // we can set todays event for anyone who has a homeLocation. If they don't we should setTodays event
            // // when they change the location drop down || or just grab the event and then compare their event code to the events
            // if (this.currentUser.authorities.indexOf('ROLE_ORGANIZER') !== -1) {
            //     this.setTodaysevent(this.currentUser.homeLocation);
            // }
            // if (this.currentUser.authorities.indexOf('ROLE_RUNNER') !== -1) {
            //     // set home location
            //     this.model.location = this.currentUser.homeLocation;
            // }

    }

    afterUserAccountSetup(user: Account) {
        this.currentUser = user;
        this.rfbEventAttendance.rfbUserId = this.currentUser.id;
        // we can set todays event for anyone who has a homeLocation. If they don't we should setTodays event
        // when they change the location drop down || or just grab the event and then compare their event code to the events
        if (this.currentUser.authorities.indexOf('ROLE_ORGANIZER') !== -1) {
            this.setTodaysevent(this.currentUser.homeLocation);
        }
        if (this.currentUser.authorities.indexOf('ROLE_RUNNER') !== -1) {
            // set home location
            this.model.location = this.currentUser.homeLocation;
        }
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    checkIn() {
        this.eventService.findByLocation(this.model.location).subscribe((rfbEvent: RfbEvent) => {
            const thisEvent = rfbEvent;
            this.rfbEventAttendance.rfbEventId = rfbEvent.id;
            if (thisEvent.eventCode === this.model.eventCode) {
                // you are checked in
                // this.rfbEventAttendanceService.create(this.rfbEventAttendance).subscribe((userCheckedIn: RfbEventAttendance) => {
                // TODO[SB] maybe wrong. Could be that it needs too be checked if response is not null
                this.rfbEventAttendanceService.create(this.rfbEventAttendance).subscribe((res: HttpResponse<RfbEventAttendance>) => {
                    this.checkedIn = true;
                });
            } else {
                this.errors.invalidEventCode = 'There is either no run today for this location or you have entered an incorrect event code. Please try again.';
            }
        });
    }

    private setTodaysevent(locationID: number) {
        // serach for event with todays date and actual location id
        this.eventService.findByLocation(locationID).subscribe(
            (rfbEvent: RfbEvent) => {
                this.todaysEvent = rfbEvent;
        });
    }

    private loadLocations() {
        this.locationService.query( {
            page: 0,
            size: 100,
            sort: ['locationName', 'ASC']}).subscribe(
            (res: HttpResponse<RfbLocation[]>) => {
                this.locations = res.body;
            },
            (error) => {
                console.log(error);
            }
        );
    }
}
