//
//  BeaconsService.m
//  Hybris
//
//  Created by zhang xiaodong on 14-11-12.
//  Copyright (c) 2014年 Red Ant. All rights reserved.
//

#import "BeaconsService.h"

#import <CoreBluetooth/CoreBluetooth.h>
#import <CoreLocation/CoreLocation.h>


@interface BeaconsService() <CLLocationManagerDelegate, CBCentralManagerDelegate> {
    BOOL _isStarted;
}

@property (strong, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) CLBeaconRegion *region;

@property (strong, nonatomic) CBCentralManager *centralManager;

@property (copy, nonatomic) void (^rangingBeaconsHandler)(NSArray *beacons, NSError *error);

@end

@implementation BeaconsService

+ (id)sharedBeaconsService {
    static BeaconsService *service;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        service = [[BeaconsService alloc] init];
        service.locationManager = [[CLLocationManager alloc] init];
        service.locationManager.delegate = service;
        service.centralManager = [[CBCentralManager alloc] initWithDelegate:service queue:dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)];
    });
    
    return service;
}

- (void)startRangingBeaconsWithUUID:(NSUUID *)uuid identifier:(NSString *)identifier rangingBeaconsHandler:(void (^)(NSArray *, NSError *))handler {
    if (_isStarted) {
        [self.locationManager stopRangingBeaconsInRegion:self.region];
        [self.locationManager stopMonitoringForRegion:self.region];
        [self.locationManager stopUpdatingLocation];
    }
    
    self.region = [[CLBeaconRegion alloc] initWithProximityUUID:uuid identifier:identifier];
    self.rangingBeaconsHandler = handler;
    
    [self startRangingBeacons];
}

-(void)startRangingBeacons
{
    _isStarted = YES;
    
    if ([CLLocationManager authorizationStatus] == CBPeripheralManagerAuthorizationStatusNotDetermined) {
        if (floor(NSFoundationVersionNumber) <= NSFoundationVersionNumber_iOS_7_1) {
            [self.locationManager startMonitoringForRegion:self.region];
            [self.locationManager startRangingBeaconsInRegion:self.region];
            [self.locationManager requestStateForRegion:self.region];
            [self.locationManager startUpdatingLocation];
        } else {
            [self.locationManager requestAlwaysAuthorization];
        }
    } else if ([CLLocationManager authorizationStatus] == CBPeripheralManagerAuthorizationStatusRestricted) {
        
    } else if ([CLLocationManager authorizationStatus] == CBPeripheralManagerAuthorizationStatusDenied) {
        
    } else if ([CLLocationManager authorizationStatus] == CBPeripheralManagerAuthorizationStatusAuthorized) {
        [self.locationManager startMonitoringForRegion:self.region];
        [self.locationManager startRangingBeaconsInRegion:self.region];
        [self.locationManager requestStateForRegion:self.region];
        [self.locationManager startUpdatingLocation];
    }
}

- (void)stopRangingBeacons {
    _isStarted = NO;
    
    [self.locationManager stopRangingBeaconsInRegion:self.region];
    [self.locationManager stopMonitoringForRegion:self.region];
    [self.locationManager stopUpdatingLocation];
    
    [self setRangingBeaconsHandler:nil];
    [self setRegion:nil];
}

- (void)centralManagerDidUpdateState:(CBCentralManager *)central {
    if (_isStarted) {
        if (self.centralManager.state == CBCentralManagerStatePoweredOn || self.centralManager.state == CBCentralManagerStateUnauthorized) {
            [self startRangingBeacons];
        } else {
            [self.locationManager stopRangingBeaconsInRegion:self.region];
            [self.locationManager stopMonitoringForRegion:self.region];
            [self.locationManager stopUpdatingLocation];
        }
    }
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status {
    [self startRangingBeacons];
}

- (void)locationManager:(CLLocationManager *)manager didRangeBeacons:(NSArray *)beacons inRegion:(CLBeaconRegion *)region {
    if (self.rangingBeaconsHandler) {
        self.rangingBeaconsHandler(beacons, nil);
    }
}

- (void)locationManager:(CLLocationManager *)manager rangingBeaconsDidFailForRegion:(CLBeaconRegion *)region withError:(NSError *)error {
    if (self.rangingBeaconsHandler) {
        self.rangingBeaconsHandler(nil, error);
    }
}

@end
